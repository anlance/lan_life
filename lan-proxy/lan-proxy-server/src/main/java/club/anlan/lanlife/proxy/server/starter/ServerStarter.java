package club.anlan.lanlife.proxy.server.starter;

import club.anlan.lanlife.proxy.server.config.ProxyConfig;
import club.anlan.lanlife.proxy.server.handler.UserChannelInHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.BindException;

/**
 * server starter
 *
 * @author lan
 * @version 1.1
 * @date 2023/7/28 21:47
 */
@Slf4j
@Component
public class ServerStarter {

    private NioEventLoopGroup serverBossGroup;
    private NioEventLoopGroup serverWorkerGroup;

    @Resource
    private ProxyConfig proxyConfig;

    /**
     * listen out
     */
    public void start() {

        serverBossGroup = new NioEventLoopGroup(1);
        serverWorkerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(serverBossGroup, serverWorkerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new UserChannelInHandler());
            }
        });
        try {
            bootstrap.bind(proxyConfig.getServicePort()).get();
            log.info("proxy server bind port {}, wait for request", proxyConfig.getServicePort());
        } catch (Exception ex) {
            log.error("server listener failed, ", ex);
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
