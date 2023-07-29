package club.anlan.lanlife.proxy.server.handler;

import club.anlan.lanlife.commponent.netty.message.ProxyMessage;
import club.anlan.lanlife.proxy.server.manager.ChannelManger;
import club.anlan.lanlife.proxy.server.util.ChannelUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;


/**
 * 处理外部请求
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/17 17:42
 */
@Slf4j
public class UserChannelInHandler extends SimpleChannelInboundHandler<ByteBuf> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        if (log.isDebugEnabled()) {
            log.debug("{} read [{}] byte data", ctx.channel(), bytes.length);
        }
        transferToProxyClient(ctx, bytes);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String userId = ChannelUtil.getChannelId(ctx);
        if (log.isDebugEnabled()) {
            log.debug("{} active", ctx.channel());
        }
        ChannelManger.addUserChannel(userId, ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String userId = ChannelUtil.getChannelId(ctx);
        if (log.isDebugEnabled()) {
            log.debug("{} inactive", ctx.channel());
        }
        ChannelManger.removeUserChannel(userId);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("{} get exception: ", ctx.channel(), cause);
        ctx.close();
    }

    /**
     * 将数据发送到内网客户端
     *
     * @param ctx  通道上下文
     * @param data 数据内容
     */
    public static void transferToProxyClient(ChannelHandlerContext ctx, byte[] data) {
        Channel cmdChannel = ChannelManger.getCmdChannel();
        String requestId = ChannelUtil.getChannelId(ctx);
        if (cmdChannel == null) {
            log.warn("no client to transfer message");
            ctx.channel().close();
        } else {
            log.info("accept request from {}", ctx.channel());
            log.info("send data to proxy client {}", cmdChannel);
            ProxyMessage proxyMessage = ProxyMessage.transferMessage(requestId, data);
            cmdChannel.writeAndFlush(proxyMessage);
        }
    }
}
