package club.anlan.lanlife.proxy.server.handler;

import club.anlan.lanlife.commponent.netty.message.ProxyMessage;
import club.anlan.lanlife.proxy.server.config.ProxyConfig;
import club.anlan.lanlife.proxy.server.manager.ProxyChannelManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;


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
        log.debug("[{}]收到数据，共[{}]字节", ctx.channel().id().asLongText(), bytes.length);
        transferToProxyClient(ctx, ProxyMessage.P_TYPE_TRANSFER, bytes);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("[{}]有请求进入", ctx.channel().id().asLongText());
        String lanInfo = ProxyConfig.getLanInfo();
        String userId = ctx.channel().id().asLongText();
        ProxyChannelManager.addUserChannel(userId, ctx.channel());
        transferToProxyClient(ctx, ProxyMessage.TYPE_CONNECT, lanInfo != null ? lanInfo.getBytes() : new byte[0]);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("[{}]有请求断开", ctx.channel().id().asLongText());
        String lanInfo = ProxyConfig.getLanInfo();
        String userId = ctx.channel().id().asLongText();
        transferToProxyClient(ctx, ProxyMessage.TYPE_DISCONNECT, lanInfo != null ? lanInfo.getBytes() : new byte[0]);
        ProxyChannelManager.removeUserChannel(userId);
        super.channelInactive(ctx);
    }

//    @Override
//    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
//        Channel userChannel = ctx.channel();
//        InetSocketAddress sa = (InetSocketAddress) userChannel.localAddress();
//        Channel cmdChannel = ProxyChannelManager.getCmdChannel();
//        if (cmdChannel == null) {
//
//            // 该端口还没有代理客户端
//            ctx.channel().close();
//        } else {
//            cmdChannel.config().setOption(ChannelOption.AUTO_READ, userChannel.isWritable());
//        }
//
//        super.channelWritabilityChanged(ctx);
//    }

    /**
     * 将数据发送到内网客户端
     *
     * @param ctx       通道上下文
     * @param type      数据类型
     * @param data      数据内容
     */
    private void transferToProxyClient(ChannelHandlerContext ctx, byte type, byte[] data) {
        // 获取代理通道
        Channel cmdChannel = ProxyChannelManager.getCmdChannel();
        if (cmdChannel == null) {
            // 该端口还没有代理客户端
            ctx.channel().close();
        } else {
            ProxyMessage proxyMessage = new ProxyMessage();
            proxyMessage.setType(type);
            proxyMessage.setData(data);
            proxyMessage.setUri(ctx.channel().id().asLongText());
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
