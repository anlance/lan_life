package club.anlan.lanlife.proxy.server.handler;

import club.anlan.lanlife.commponent.netty.message.ProxyMessage;
import club.anlan.lanlife.proxy.server.config.ProxyConfig;
import club.anlan.lanlife.proxy.server.manager.ProxyChannelManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * ServerChannelHandler
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/17 16:24
 */
@Slf4j
public class ServerChannelHandler extends SimpleChannelInboundHandler<ProxyMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProxyMessage proxyMessage) throws Exception {
        switch (proxyMessage.getType()) {
            case ProxyMessage.C_TYPE_AUTH:
                handleAuthMessage(ctx, proxyMessage);
                break;
            case ProxyMessage.TYPE_CONNECT:
                handleConnectMessage(ctx, proxyMessage);
                break;
            case ProxyMessage.TYPE_DISCONNECT:
                handleDisconnectMessage(ctx, proxyMessage);
                break;
            case ProxyMessage.P_TYPE_TRANSFER:
                handleTransferMessage(ctx, proxyMessage);
                break;
            case ProxyMessage.TYPE_HEARTBEAT:
                handleHeartbeatMessage(ctx, proxyMessage);
                break;
            default:
                break;
        }
    }

    private void handleTransferMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        ByteBuf buf = ctx.alloc().buffer(proxyMessage.getData().length);
        Channel userChannel = ProxyChannelManager.getUserChannel(proxyMessage.getUri());
        buf.writeBytes(proxyMessage.getData());
        if (userChannel != null) {
            userChannel.writeAndFlush(buf);
        }
    }

    private void handleDisconnectMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        if (proxyMessage.getUri() != null) {
            Channel userChannel = ProxyChannelManager.getUserChannel(proxyMessage.getUri());
            ProxyChannelManager.removeUserChannel(proxyMessage.getUri());
            if (userChannel != null) {
                userChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            }
            return;
        }

        Channel cmdChannel = ProxyChannelManager.getCmdChannel();
        if (cmdChannel == null) {
            log.warn("ConnectMessage:error cmd channel key {}", proxyMessage.getUri());
        }
    }

    private void handleConnectMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        log.debug("{} 客户端请求连接", ctx.channel());
        String uri = proxyMessage.getUri();
        if (uri == null) {
            log.warn("ConnectMessage:null uri");
            ctx.channel().close();
        }
    }

    private void handleHeartbeatMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        ProxyMessage heartbeatMessage = new ProxyMessage();
        heartbeatMessage.setSerialNumber(proxyMessage.getSerialNumber());
        heartbeatMessage.setType(ProxyMessage.TYPE_HEARTBEAT);
        log.debug("response heartbeat message {} - {}", ctx.channel(), heartbeatMessage);
        ctx.channel().writeAndFlush(heartbeatMessage);
    }

    private void handleAuthMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        String clientKey = proxyMessage.getUri();
        String msg = "auth success";
        if (ProxyConfig.isLegalClientKey(clientKey)) {
            Integer port = ProxyConfig.getServerPort();
            if (port == null) {
                msg = "client key is invalid!";
                log.error("{} no server port init", ctx.channel());
            } else {
                Channel channel = ProxyChannelManager.getCmdChannel(clientKey);
                if (channel != null) {
                    msg = "client key is invalid!";
                    log.error("exist channel for key {}, {}", clientKey, channel);
                } else {
                    // 认证成功
                    log.info("set port => channel, {}, {}, {}", clientKey, port, ctx.channel());
                    ProxyChannelManager.addCmdChannel(port, clientKey, ctx.channel());
                    ctx.channel().writeAndFlush(ProxyMessage.authSuccessMessage(msg.getBytes()));
                    return;
                }
            }
        }
        msg = "client key is invalid!";
        log.error("{} error clientKey {}", ctx.channel(), clientKey);
        ctx.channel().writeAndFlush(ProxyMessage.authFailedMessage(msg.getBytes()));
        ctx.channel().close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("--------- channelInactive");
        Channel channel = ctx.channel();
        if (channel != null && channel.isActive()) {
            // 数据发送完成后再关闭连接，解决http1.0数据传输问题
            channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            channel.close();
        } else {
            ProxyChannelManager.removeCmdChannel(ctx.channel());
        }
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exception caught", cause);
        ProxyChannelManager.removeCmdChannel();
        super.exceptionCaught(ctx, cause);
    }
}
