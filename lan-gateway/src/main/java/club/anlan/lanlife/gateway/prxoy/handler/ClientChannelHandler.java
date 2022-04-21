package club.anlan.lanlife.gateway.prxoy.handler;

import club.anlan.lanlife.commponent.netty.constant.Constant;
import club.anlan.lanlife.commponent.netty.message.ProxyMessage;
import club.anlan.lanlife.gateway.config.ProxyConfig;
import club.anlan.lanlife.gateway.prxoy.manager.ClientChannelManager;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * ClientChannelHandler
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/17 19:43
 */
@Slf4j
public class ClientChannelHandler extends SimpleChannelInboundHandler<ProxyMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProxyMessage proxyMessage) throws Exception {
        log.debug("received proxy message, type is {}", proxyMessage.getType());
        byte[] data = proxyMessage.getData();
        if (data != null) {
            log.debug("data length: {}", data.length);
        }
        switch (proxyMessage.getType()) {
            case ProxyMessage.TYPE_CONNECT:
                handleConnectMessage(ctx, proxyMessage);
                break;
            case ProxyMessage.TYPE_DISCONNECT:
                handleDisconnectMessage(ctx, proxyMessage);
                break;
            case ProxyMessage.P_TYPE_TRANSFER:
                handleTransferMessage(ctx, proxyMessage);
                break;
            default:
                break;
        }
    }

    private void handleTransferMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        ByteBuf buf = ctx.alloc().buffer(proxyMessage.getData().length);
        String clientId = ctx.channel().id().asLongText();
        buf.writeBytes(proxyMessage.getData());
        log.info("[{}] server 收到 message :{}", clientId, proxyMessage);
        log.info("server Length: {}, data: {}", proxyMessage.getData().length, buf.toString(CharsetUtil.UTF_8));
        // 新建连接并发送数据
        Channel localChannel = ClientChannelManager.getLocalChannel();
        ClientChannelManager.setRemoteId(localChannel.id().asLongText(), proxyMessage.getUri());
        localChannel.writeAndFlush(buf);
//        ctx.channel().writeAndFlush(ProxyMessage.transferMessage(proxyMessage.getUri(), "sho".getBytes()));
    }

    private void handleDisconnectMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        Channel cmdChannel = ctx.channel().attr(Constant.NEXT_CHANNEL).get();
        log.info("handleDisconnectMessage, {}", cmdChannel);
        if (cmdChannel != null) {
            ctx.channel().attr(Constant.NEXT_CHANNEL).remove();
            cmdChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void handleConnectMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        String ip = ProxyConfig.getThisServerHost();
        Integer port = ProxyConfig.getThisServerPort();
        log.debug("connect real server [{}:{}] success, {}", ip, port, ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("与代理服务器连接断开");
        // 控制连接
        if (ClientChannelManager.getCmdChannel() == ctx.channel()) {
            ClientChannelManager.setCmdChannel(null);
//            ClientChannelManager.channelInactive(ctx);
        } else {
            // 数据传输连接
            Channel cmdChannel = ctx.channel().attr(Constant.NEXT_CHANNEL).get();
            if (cmdChannel != null && cmdChannel.isActive()) {
                cmdChannel.close();
            }
        }

//        ClientChannelManager.removeLocalChanel(ctx.channel());
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exception caught", cause);
        super.exceptionCaught(ctx, cause);
    }

}
