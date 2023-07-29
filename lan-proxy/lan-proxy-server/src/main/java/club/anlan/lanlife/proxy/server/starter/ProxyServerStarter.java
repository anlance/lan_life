package club.anlan.lanlife.proxy.server.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * NettyStarter
 *
 * @author lan
 * @version 1.1
 * @date 2022/4/17 17:34
 */
@Slf4j
@Component
public class ProxyServerStarter {

    @Resource
    private ServerStarter serverStarter;

    @Resource
    private ProxyStarter proxyStarter;

    public void start() {
        // 监听客户端的连接
        proxyStarter.start();
        // 监听外部的请求
        serverStarter.start();
    }


    public void stop() {
        serverStarter.stop();
        proxyStarter.stop();
    }
}
