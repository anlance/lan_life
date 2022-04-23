package club.anlan.lanlife.proxy.server.handler;

import club.anlan.lanlife.commponent.netty.message.ProxyMessage;
import club.anlan.lanlife.proxy.server.config.ProxyConfig;
import club.anlan.lanlife.proxy.server.manager.ProxyChannelManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * 处理外部请求
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/17 17:42
 */
@Slf4j
public class UserChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        // 通知代理客户端
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        log.debug("[{}]收到数据，共[{}]字节,数据是：{}", ctx.channel().id().asLongText(), bytes.length, buf.toString(CharsetUtil.UTF_8));
        transferToProxyClient(ctx, ProxyMessage.P_TYPE_TRANSFER, bytes, ctx.channel().id().asLongText());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("[{}]有请求进入", ctx.channel().id().asLongText());
        String lanInfo = ProxyConfig.getLanInfo();
        String userId = ctx.channel().id().asLongText();
        ProxyChannelManager.addUserChannel(userId, ctx.channel());
        transferToProxyClient(ctx, ProxyMessage.TYPE_CONNECT, lanInfo != null ? lanInfo.getBytes() : new byte[0], userId);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("[{}]有请求断开", ctx.channel().id().asLongText());
        String lanInfo = ProxyConfig.getLanInfo();
        String userId = ctx.channel().id().asLongText();
//        transferToProxyClient(ctx, ProxyMessage.TYPE_DISCONNECT, lanInfo != null ? lanInfo.getBytes() : new byte[0], userId);
        ProxyChannelManager.removeUserChannel(userId);
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
        Channel cmdChannel = ProxyChannelManager.getCmdChannel();
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
