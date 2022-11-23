//package club.anlan.lanlife.gateway.prxoy.handler;
//
//import club.anlan.lanlife.commponent.netty.constant.Constant;
//import club.anlan.lanlife.commponent.netty.message.ProxyMessage;
//import club.anlan.lanlife.gateway.config.ProxyConfig;
//import club.anlan.lanlife.gateway.prxoy.manager.ClientChannelManager;
//import io.netty.bootstrap.Bootstrap;
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.*;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.http.HttpObjectAggregator;
//import io.netty.handler.codec.http.HttpResponseEncoder;
//import io.netty.handler.stream.ChunkedWriteHandler;
//import io.netty.util.CharsetUtil;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.Objects;
//
///**
// * ClientChannelHandler
// *
// * @author lan
// * @version 1.0
// * @date 2022/4/17 19:43
// */
//@Slf4j
//public class ClientChannelHandler extends SimpleChannelInboundHandler<ProxyMessage> {
//
//    private NioEventLoopGroup workerGroup;
//
//    public ClientChannelHandler(NioEventLoopGroup workerGroup){
//        this.workerGroup = workerGroup;
//    }
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, ProxyMessage proxyMessage) throws Exception {
//        byte[] data = proxyMessage.getData();
//        if (data != null) {
//            log.debug("data length: {}", data.length);
//        }
//        switch (proxyMessage.getType()) {
//            case ProxyMessage.TYPE_CONNECT:
//                handleConnectMessage(ctx, proxyMessage);
//                break;
//            case ProxyMessage.TYPE_DISCONNECT:
//                handleDisconnectMessage(ctx, proxyMessage);
//                break;
//            case ProxyMessage.P_TYPE_TRANSFER:
//                handleTransferMessage(ctx, proxyMessage);
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void handleTransferMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
//
//        String ip = ProxyConfig.getLocalHost();
//        Integer port = ProxyConfig.getLocalPort();
//        Bootstrap localBootstrap = new Bootstrap();
//        localBootstrap.group(workerGroup)
//                .channel(NioSocketChannel.class)
//                .handler(new ChannelInitializer<SocketChannel>() {
//                    @Override
//                    protected void initChannel(SocketChannel ch) throws Exception {
//                        ch.pipeline().addLast(new LocalChannelHandler());
//
//                    }
//                })
//                .connect(ip, port).addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture channelFuture) throws Exception {
//                if (channelFuture.isSuccess()) {
//                    ClientChannelManager.setLocalChannel(proxyMessage.getUri(), channelFuture.channel());
//                    ClientChannelManager.setRemoteId(channelFuture.channel().id().asLongText(), proxyMessage.getUri());
//                    log.debug("connect real server [{}:{}] success, {}", ip, port, ctx.channel());
//                    ByteBuf buf = ctx.alloc().buffer(proxyMessage.getData().length);
//                    buf.writeBytes(proxyMessage.getData());
//                    log.debug("server Length: {}", proxyMessage.getData().length);
//                    channelFuture.channel().writeAndFlush(buf);
//                } else {
//                    log.warn("连接本地端口 failed", channelFuture.cause());
//                }
//            }
//        });
//    }
//
//    private void handleDisconnectMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
//        Channel localChannel = ClientChannelManager.getLocalChannel(proxyMessage.getUri());
//        log.info("handleDisconnectMessage, {}", localChannel);
//        if (localChannel != null) {
//            localChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
//        }
//    }
//
//    private void handleConnectMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
//
//    }
//
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        log.debug("与代理服务器连接断开");
//        if (ClientChannelManager.getCmdChannel() == ctx.channel()) {
//            ClientChannelManager.setCmdChannel(null);
//        } else {
//            Channel cmdChannel = ClientChannelManager.getCmdChannel();
//            if (cmdChannel != null && cmdChannel.isActive()) {
//                cmdChannel.close();
//            }
//        }
//        super.channelInactive(ctx);
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        log.error("exception caught", cause);
//        ClientChannelManager.removeCmdChannel();
//        super.exceptionCaught(ctx, cause);
//    }
//
//}
