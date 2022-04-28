package club.anlan.lanlife.proxy.server.config;

import club.anlan.lanlife.component.base.spring.AppContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * 代理服务端配置信息
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/17 16:48
 */
@Slf4j
@Data
@Configuration
public class ProxyConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Value("${config.server.host:127.0.0.1}")
    private String host;

    /**
     * 代理服务器与代理客户端通信端口
     */
    @Value("${config.server.port:8082}")
    private Integer configServerPort;

    /**
     * 当前只支持一个客户端
     * 外网访问代理服务器的端口，通过该端口的数据都将转发给代理客户端
     */
    @Value("${config.server.service.port:8088}")
    private Integer servicePort;

    /**
     * 配置的客户端的 key
     */
    @Value("${config.server.client-key:lan-local}")
    private String clientKey;

    /**
     * 获取客户端绑定的serverPort
     *
     * @return serverPort
     */
    public static Integer getServerPort() {
        ProxyConfig proxyConfig = AppContext.getBean(ProxyConfig.class);
        if (proxyConfig != null) {
            return proxyConfig.getConfigServerPort();
        }
        return null;
    }

    /**
     * 获取服务端的 ip 和端口信息
     *
     * @return 服务端信息
     */
    public static String getLanInfo() {
        ProxyConfig proxyConfig = AppContext.getBean(ProxyConfig.class);
        if (proxyConfig != null) {
            return proxyConfig.getHost() + ":" + proxyConfig.getConfigServerPort();
        }
        return null;
    }

    /**
     * 客户端Id是否合法
     *
     * @param clientKey 客户端id
     * @return true ? 合法 : 不合法
     */
    public static boolean isLegalClientKey(String clientKey) {
        ProxyConfig proxyConfig = AppContext.getBean(ProxyConfig.class);
        if (proxyConfig != null) {
            String ck = proxyConfig.getClientKey();
            if (ck.equals(clientKey)) {
                return true;
            }
        }
        return false;
    }
}
