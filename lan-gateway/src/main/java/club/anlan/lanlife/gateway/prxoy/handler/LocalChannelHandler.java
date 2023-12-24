package club.anlan.lanlife.gateway.prxoy.handler;

import club.anlan.lanlife.commponent.netty.message.ProxyMessage;
import club.anlan.lanlife.gateway.prxoy.manager.ClientChannelManager;
import club.anlan.lanlife.gateway.prxoy.manager.LocalChannelManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 本地 channel handler
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/21 22:52
 */
@Slf4j
public class LocalChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        transferToProxyServer(ctx, bytes);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("{} active", ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("{} inactive", ctx.channel());
        LocalChannelManager.removeLocalChannel(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("{} exception, ", ctx.channel(), cause);
        LocalChannelManager.removeLocalChannel(ctx.channel());
        ctx.close();
    }

    /**
     * 将数据发送到代理服务器
     *
     * @param ctx  通道上下文
     * @param data 数据内容
     */
    public static void transferToProxyServer(ChannelHandlerContext ctx, byte[] data) {
        String requestId = ClientChannelManager.getRemoteId(ctx.channel().id().asLongText());
        Channel proxyChannel = ClientChannelManager.getProxyChannel();
        if (proxyChannel == null) {
            ctx.channel().close();
        } else {
            ProxyMessage proxyMessage = ProxyMessage.transferMessage(requestId, data);
            log.info("accept response from {}", ctx.channel());
            log.info("send data to proxy server {}", proxyChannel);
            proxyChannel.writeAndFlush(proxyMessage);
        }
    }
}
