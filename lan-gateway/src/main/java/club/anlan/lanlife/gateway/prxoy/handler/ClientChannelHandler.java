package club.anlan.lanlife.gateway.prxoy.handler;

import club.anlan.lanlife.commponent.netty.message.ProxyMessage;
import club.anlan.lanlife.gateway.config.ProxyConfig;
import club.anlan.lanlife.gateway.prxoy.manager.ClientChannelManager;
import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
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

    private NioEventLoopGroup workerGroup;

    public ClientChannelHandler(NioEventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProxyMessage proxyMessage) throws Exception {
        log.info("{} msg type: {}", ctx.channel(), proxyMessage.getType());
        switch (proxyMessage.getType()) {
            case ProxyMessage.C_TYPE_AUTH_SUCCESS:
                handleAuthSuccessMessage(ctx);
                break;
            case ProxyMessage.P_TYPE_TRANSFER:
                handleTransferMessage(ctx, proxyMessage);
                break;
            default:
                break;
        }
    }

    private void handleTransferMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {

        String ip = ProxyConfig.getLocalHost();
        Integer port = ProxyConfig.getLocalPort();
        Bootstrap localBootstrap = new Bootstrap();
        localBootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LocalChannelHandler());

                    }
                })
                .connect(ip, port).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (channelFuture.isSuccess()) {
                            // todo 连接池
                            ClientChannelManager.setRemoteId(channelFuture.channel().id().asLongText(), proxyMessage.getRequestId());
                            log.debug("connect local server [{}:{}] success, {}", ip, port, ctx.channel());
                            log.info("data byte: {}", proxyMessage.getData().length);
                            ByteBuf buf = ctx.alloc().buffer(proxyMessage.getData().length);
                            buf.writeBytes(proxyMessage.getData());
                            channelFuture.channel().writeAndFlush(buf);
                        } else {
                            log.warn("connect local server failed, ", channelFuture.cause());
                        }
                    }
                });
    }

    public static void handleAuthSuccessMessage(ChannelHandlerContext ctx) {
        ClientChannelManager.setProxyChannel(ctx.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("{} active", ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("{} inactive", ctx.channel());
        if (ClientChannelManager.getProxyChannel() == ctx.channel()) {
            ClientChannelManager.setProxyChannel(null);
        } else {
            Channel cmdChannel = ClientChannelManager.getProxyChannel();
            if (cmdChannel != null && cmdChannel.isActive()) {
                cmdChannel.close();
            }
        }
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("{} exception, ", ctx.channel(), cause);
        ClientChannelManager.removeCmdChannel();
        super.exceptionCaught(ctx, cause);
    }

}
