package club.anlan.lanlife.gateway.prxoy;

import club.anlan.lanlife.commponent.netty.constant.Constant;
import club.anlan.lanlife.commponent.netty.handler.ProxyMessageDecoder;
import club.anlan.lanlife.commponent.netty.handler.ProxyMessageEncoder;
import club.anlan.lanlife.commponent.netty.message.ProxyMessage;
import club.anlan.lanlife.gateway.config.ProxyConfig;
import club.anlan.lanlife.gateway.prxoy.handler.ClientChannelHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ProxyClientStarter
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/17 18:42
 */
@Slf4j
@Component
public class ProxyClientStarter {

    private NioEventLoopGroup workerGroup;

    private Bootstrap bootstrap;

    private long sleepTimeMill = 1000;

    private int maxTryTimes = 0;

    @Autowired
    private ProxyConfig proxyConfig;

    public void start() {
        bootstrap = new Bootstrap();
        workerGroup = new NioEventLoopGroup();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ProxyMessageDecoder(Constant.MAX_FRAME_LENGTH, Constant.LENGTH_FIELD_OFFSET, Constant.LENGTH_FIELD_LENGTH, Constant.LENGTH_ADJUSTMENT, Constant.INITIAL_BYTES_TO_STRIP));
                        ch.pipeline().addLast(new ProxyMessageEncoder());
                        //ch.pipeline().addLast(new IdleCheckHandler(Constant.READ_IDLE_TIME, Constant.WRITE_IDLE_TIME - 10, 0));
                        ch.pipeline().addLast(new ClientChannelHandler(workerGroup));
                    }
                });
        connectProxyServer();
    }

    /**
     * 连接代理服务器
     */
    public void connectProxyServer() {
        bootstrap.connect(proxyConfig.getServerHost(), proxyConfig.getServerPort()).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    log.info("try connect proxy server success, {} then fetch auth...", future.channel());
                    // 连接成功，向服务器发送客户端认证信息（clientKey）
                    ProxyMessage proxyMessage = new ProxyMessage();
                    proxyMessage.setType(ProxyMessage.C_TYPE_AUTH);
                    proxyMessage.setRequestId(proxyConfig.getKey());
                    proxyMessage.setSerialNumber(1L);
                    future.channel().writeAndFlush(proxyMessage);
                    maxTryTimes = 0;
                } else {
                    log.warn("try connect proxy server failed", future.cause());
                    // 连接失败，发起重连
                    if (maxTryTimes < 3) {
                        reconnectWait();
                        connectProxyServer();
                        maxTryTimes += 1;
                    }
                }
            }
        });
    }


    public void stop() {
        workerGroup.shutdownGracefully();
    }


    private void reconnectWait() {
        try {
            synchronized (this) {
                wait(sleepTimeMill);
            }
        } catch (InterruptedException e) {
        }
    }
}
