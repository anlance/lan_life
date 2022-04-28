package club.anlan.lanlife.gateway.prxoy.handler;

import club.anlan.lanlife.commponent.netty.message.ProxyMessage;
import club.anlan.lanlife.gateway.config.ProxyConfig;
import club.anlan.lanlife.gateway.prxoy.manager.ClientChannelManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
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
        // 通知代理客户端
        byte[] bytes = new byte[buf.readableBytes()];
        log.debug("[{}]收到数据，共[{}]字节", ctx.channel().id().asLongText(), bytes.length);
        buf.readBytes(bytes);
        String remoteId = ClientChannelManager.getRemoteId(ctx.channel().id().asLongText());
        transferToProxyClient(ctx, ProxyMessage.P_TYPE_TRANSFER, bytes, remoteId);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("[{}]有请求进入", ctx.channel().id().asLongText());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("[{}]有请求断开", ctx.channel().id().asLongText());
        String userId = ctx.channel().id().asLongText();
//        transferToProxyClient(ctx, ProxyMessage.TYPE_DISCONNECT, new byte[0], userId);
    }

    /**
     * 将数据发送到内网客户端
     *
     * @param ctx       通道上下文
     * @param type      数据类型
     * @param data      数据内容
     * @param channelId 每个请求都是一个channel，每个channel有唯一id
     */
    private void transferToProxyClient(ChannelHandlerContext ctx, byte type, byte[] data, String channelId) {
        // 获取代理通道
        Channel cmdChannel = ClientChannelManager.getCmdChannel();
        if (cmdChannel == null) {
            // 该端口还没有代理客户端
            ctx.channel().close();
        } else {
            ProxyMessage proxyMessage = new ProxyMessage();
            proxyMessage.setType(type);
            proxyMessage.setData(data);
            proxyMessage.setUri(channelId);
            cmdChannel.writeAndFlush(proxyMessage);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当出现异常就关闭连接
        log.error("[{" + ctx.channel().id().asLongText() + "}]出现异常: " + cause);
        ctx.close();
    }
}
