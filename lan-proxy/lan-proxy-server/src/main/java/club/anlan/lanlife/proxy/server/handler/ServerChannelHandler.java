package club.anlan.lanlife.proxy.server.handler;

import club.anlan.lanlife.commponent.netty.message.ProxyMessage;
import club.anlan.lanlife.component.utils.UUIDUtil;
import club.anlan.lanlife.proxy.server.config.ProxyConfig;
import club.anlan.lanlife.proxy.server.manager.ChannelManger;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

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
        log.info("channel :{}, msg type: {}", ctx.channel(), proxyMessage.getType());
        switch (proxyMessage.getType()) {
            case ProxyMessage.C_TYPE_AUTH:
                handleAuthMessage(ctx, proxyMessage);
                break;
            case ProxyMessage.P_TYPE_TRANSFER:
                handleTransferMessage(ctx, proxyMessage);
                break;
            case ProxyMessage.TYPE_HEARTBEAT:
                handleHeartBeatMessage(ctx, proxyMessage);
            default:
                break;
        }
    }

    private void handleHeartBeatMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        log.info("receive client heartbeat");
        ctx.channel().writeAndFlush(proxyMessage);
    }

    private void handleAuthMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        String clientKey = proxyMessage.getRequestId();
        if (ProxyConfig.isLegalClientKey(clientKey)) {
            Integer port = ProxyConfig.getServerPort();
            if (port == null) {
                log.error("{} no server port init", ctx.channel());
            } else {
                Channel channel = ChannelManger.getCmdChannel(clientKey);
                if (channel != null) {
                    log.error("exist channel for key {}, {}", clientKey, channel);
                    channel.close();
                }
                log.info("set port => channel, {}, {}, {}", clientKey, port, ctx.channel());
                ChannelManger.addCmdChannel(clientKey, ctx);
                proxyMessage.setType(ProxyMessage.C_TYPE_AUTH_SUCCESS);
                ctx.writeAndFlush(proxyMessage);
                return;
            }
        }
        String msg = "client key is invalid!";
        log.error("{} error clientKey {}", ctx.channel(), clientKey);
        ctx.channel().writeAndFlush(ProxyMessage.authFailedMessage(msg.getBytes()));
        ctx.channel().close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("{}  active", ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("{}  Inactive", ctx.channel());
        Channel channel = ctx.channel();
        if (channel != null && channel.isActive()) {
            // 数据发送完成后再关闭连接，解决http1.0数据传输问题
            channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            channel.close();
        } else {
            ChannelManger.removeCmdChannel(ctx.channel());
        }
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exception caught, ", cause);
        ChannelManger.removeCmdChannel();
        super.exceptionCaught(ctx, cause);
    }

    /**
     * 将数据发到外部
     */
    public static void handleTransferMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        Channel userChannel = ChannelManger.getUserChannel(proxyMessage.getRequestId());
        if (userChannel != null) {
            ByteBuf buf = ctx.alloc().buffer(proxyMessage.getData().length);
            buf.writeBytes(proxyMessage.getData());
            log.info("send data to user client {}", userChannel);
            log.info("data byte {}", proxyMessage.getData().length);
            userChannel.writeAndFlush(buf);
        }
    }
}
