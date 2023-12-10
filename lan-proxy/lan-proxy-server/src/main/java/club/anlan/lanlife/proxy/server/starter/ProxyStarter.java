package club.anlan.lanlife.proxy.server.starter;

import club.anlan.lanlife.commponent.netty.constant.Constant;
import club.anlan.lanlife.commponent.netty.handler.IdleCheckHandler;
import club.anlan.lanlife.commponent.netty.handler.ProxyMessageDecoder;
import club.anlan.lanlife.commponent.netty.handler.ProxyMessageEncoder;
import club.anlan.lanlife.commponent.netty.message.ProxyMessage;
import club.anlan.lanlife.proxy.server.config.ProxyConfig;
import club.anlan.lanlife.proxy.server.handler.ServerChannelHandler;
import club.anlan.lanlife.proxy.server.manager.ChannelManger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * proxy starter
 *
 * @author lan
 * @version 1.1
 * @date 2023/7/28 21:47
 */
@Slf4j
@Component
public class ProxyStarter {

    private NioEventLoopGroup serverBossGroup;
    private NioEventLoopGroup serverWorkerGroup;

    @Resource
    private ProxyConfig proxyConfig;

    /**
     * start proxy server, wait client
     */
    public void start() {

        serverBossGroup = new NioEventLoopGroup(1);
        serverWorkerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(serverBossGroup, serverWorkerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ProxyMessageDecoder(Constant.MAX_FRAME_LENGTH, Constant.LENGTH_FIELD_OFFSET, Constant.LENGTH_FIELD_LENGTH, Constant.LENGTH_ADJUSTMENT, Constant.INITIAL_BYTES_TO_STRIP));
                        ch.pipeline().addLast(new ProxyMessageEncoder());
                        ch.pipeline().addLast(new IdleCheckHandler(Constant.SERVER_READ_IDLE_TIME, Constant.SERVER_WRITE_IDLE_TIME, 0));
                        ch.pipeline().addLast(new ServerChannelHandler());
                    }
                });

        try {
            bootstrap.bind(proxyConfig.getHost(), proxyConfig.getConfigServerPort()).get();
            log.info("proxy server start on {}:{}, wait client to connect", proxyConfig.getHost(), proxyConfig.getConfigServerPort());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void stop() {
        serverBossGroup.shutdownGracefully();
        serverWorkerGroup.shutdownGracefully();
    }
}
