package club.anlan.lanlife.gateway.job;

import club.anlan.lanlife.gateway.prxoy.ProxyClientStarter;
import club.anlan.lanlife.gateway.prxoy.manager.ClientChannelManager;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Objects;

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
     * 定时方法  corn表达式为 每5s执行一次
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void timerTest() {
        Channel cmdChannel = ClientChannelManager.getCmdChannel();
        if (cmdChannel == null || !cmdChannel.isActive()) {
            log.warn("重新连接代理服务器");
            proxyClientStarter.connectProxyServer();
        }
    }
}
