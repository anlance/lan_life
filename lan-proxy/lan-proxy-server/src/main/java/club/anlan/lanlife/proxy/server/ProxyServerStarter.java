package club.anlan.lanlife.proxy.server;

import club.anlan.lanlife.commponent.netty.constant.Constant;
import club.anlan.lanlife.commponent.netty.handler.IdleCheckHandler;
import club.anlan.lanlife.commponent.netty.handler.ProxyMessageDecoder;
import club.anlan.lanlife.commponent.netty.handler.ProxyMessageEncoder;
import club.anlan.lanlife.proxy.server.config.ProxyConfig;
import club.anlan.lanlife.proxy.server.handler.ServerChannelHandler;
import club.anlan.lanlife.proxy.server.handler.UserChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.BindException;

/**
 * NettyStarter
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/17 17:34
 */
@Slf4j
@Component
public class ProxyServerStarter {

    private NioEventLoopGroup serverBossGroup;

    private NioEventLoopGroup serverWorkerGroup;

    @Autowired
    private ProxyConfig proxyConfig;

    public void start() {
        // 监听客户端的连接
        ServerBootstrap bootstrap = new ServerBootstrap();
        serverBossGroup = new NioEventLoopGroup();
        serverWorkerGroup = new NioEventLoopGroup();
        bootstrap.group(serverBossGroup, serverWorkerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ProxyMessageDecoder(Constant.MAX_FRAME_LENGTH, Constant.LENGTH_FIELD_OFFSET, Constant.LENGTH_FIELD_LENGTH, Constant.LENGTH_ADJUSTMENT, Constant.INITIAL_BYTES_TO_STRIP));
                        ch.pipeline().addLast(new ProxyMessageEncoder());
                        ch.pipeline().addLast(new IdleCheckHandler(Constant.READ_IDLE_TIME, Constant.WRITE_IDLE_TIME, 0));
                        ch.pipeline().addLast(new ServerChannelHandler());
                    }
                });

        try {
            bootstrap.bind(proxyConfig.getHost(), proxyConfig.getConfigServerPort()).get();
            log.info("proxy server start on port {}, wait client to connect", proxyConfig.getConfigServerPort());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        // 监听外部的请求
        startUserPort();
    }

    private void startUserPort() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(serverBossGroup, serverWorkerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new UserChannelHandler());
                    }
                });

        try {
            bootstrap.bind(proxyConfig.getServicePort()).get();
            log.info("proxy server bind port {}, wait request", proxyConfig.getServicePort());
        } catch (Exception ex) {
            // BindException表示该端口已经绑定过
            if (!(ex.getCause() instanceof BindException)) {
                throw new RuntimeException(ex);
            }
        }

    }

    public void stop() {
        serverBossGroup.shutdownGracefully();
        serverWorkerGroup.shutdownGracefully();
    }
}
