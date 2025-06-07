package club.anlan.lanlife.gateway.job;

import club.anlan.lanlife.gateway.prxoy.ProxyClientStarter;
import club.anlan.lanlife.gateway.prxoy.manager.ClientChannelManager;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 定时检查是否和代理服务连接
 *
 * @author lan
 * @version 1.0
 * @date 2022/5/3 16:57
 */
@Slf4j
@SpringBootConfiguration
@EnableScheduling
public class ConnectToProxyServerJob {

    @Autowired
    private ProxyClientStarter proxyClientStarter;

    /**
     * 定时方法  每 3min 执行一次
     */
    //@Scheduled(cron = "0 0/3 * * * ?")
    public void timerTest() {
        Channel cmdChannel = ClientChannelManager.getProxyChannel();
        if (cmdChannel == null || !cmdChannel.isActive()) {
            log.warn("重新连接代理服务器");
            proxyClientStarter.connectProxyServer();
        }
    }
}
