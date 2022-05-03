package club.anlan.lanlife.gateway.prxoy;

import club.anlan.lanlife.commponent.netty.constant.Constant;
import club.anlan.lanlife.commponent.netty.handler.IdleCheckHandler;
import club.anlan.lanlife.commponent.netty.handler.ProxyMessageDecoder;
import club.anlan.lanlife.commponent.netty.handler.ProxyMessageEncoder;
import club.anlan.lanlife.commponent.netty.message.ProxyMessage;
import club.anlan.lanlife.gateway.config.ProxyConfig;
import club.anlan.lanlife.gateway.prxoy.handler.ClientChannelHandler;
import club.anlan.lanlife.gateway.prxoy.manager.ClientChannelManager;
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
                        ch.pipeline().addLast(new IdleCheckHandler(Constant.READ_IDLE_TIME, Constant.WRITE_IDLE_TIME - 10, 0));
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
                    // 连接成功，向服务器发送客户端认证信息（clientKey）
                    ClientChannelManager.setCmdChannel(future.channel());
                    ProxyMessage proxyMessage = new ProxyMessage();
                    proxyMessage.setType(ProxyMessage.C_TYPE_AUTH);
                    proxyMessage.setUri(proxyConfig.getKey());
                    future.channel().writeAndFlush(proxyMessage);
                    sleepTimeMill = 1000;
                    log.info("connect proxy server {}:{} success, {}", proxyConfig.getServerHost(), proxyConfig.getServerPort(), future.channel());
                } else {
                    log.warn("connect proxy server failed", future.cause());
                    // 连接失败，发起重连
                    reconnectWait();
                    connectProxyServer();
                }
            }
        });
    }


    public void stop() {
        workerGroup.shutdownGracefully();
    }


    private void reconnectWait() {
        try {
            if (sleepTimeMill > 60000) {
                sleepTimeMill = 1000;
            }

            synchronized (this) {
                sleepTimeMill = sleepTimeMill * 2;
                wait(sleepTimeMill);
            }
        } catch (InterruptedException e) {
        }
    }
}
