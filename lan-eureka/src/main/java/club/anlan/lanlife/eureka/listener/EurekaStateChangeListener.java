package club.anlan.lanlife.eureka.listener;

import com.netflix.appinfo.InstanceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.eureka.server.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


/**
 * Eureka事件监听
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/10 14:13
 */
@Component
public class EurekaStateChangeListener {

    private static final Logger logger = LoggerFactory.getLogger(EurekaStateChangeListener.class);

    @EventListener
    public void listen(EurekaInstanceCanceledEvent event) {
        logger.info(event.getServerId() + "\t" + event.getAppName() + " 服务下线");
    }

    @EventListener
    public void listen(EurekaInstanceRegisteredEvent event) {
        InstanceInfo instanceInfo = event.getInstanceInfo();
        logger.info(instanceInfo.getAppName() + "进行注册");
    }

    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
        logger.info(event.getServerId() + "\t" + event.getAppName() + " 服务进行续约");
    }

    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {
        logger.info("注册中心 启动");
    }

    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        logger.info("Eureka Server 启动");
    }
}
