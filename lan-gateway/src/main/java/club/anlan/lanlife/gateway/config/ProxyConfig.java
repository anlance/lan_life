//package club.anlan.lanlife.gateway.config;
//
//import club.anlan.lanlife.component.base.spring.AppContext;
//import lombok.Data;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//
///**
// * ProxyConfig
// *
// * @author lan
// * @version 1.0
// * @date 2022/4/17 19:46
// */
//@Data
//@Configuration
//public class ProxyConfig {
//
//    @Value("${config.server.host:127.0.0.1}")
//    private String serverHost;
//
//    /**
//     * 代理服务器与代理客户端通信端口
//     */
//    @Value("${config.server.port:8082}")
//    private Integer serverPort;
//
//    /**
//     * 客户端 key
//     */
//    @Value("${config.client.key:lan-local}")
//    private String key;
//
//    @Value("${config.service.host:127.0.0.1}")
//    private String serviceHost;
//
//    /**
//     * 代理端口 , nginx 端口
//     */
//    @Value("${config.service.port:80}")
//    private Integer servicePort;
//
//    /**
//     * 获取客户端的key
//     */
//    public static String getClientKey() {
//        ProxyConfig proxyConfig = AppContext.getBean(ProxyConfig.class);
//        if (proxyConfig != null) {
//            return proxyConfig.getKey();
//        }
//        return null;
//    }
//
//    public static String getThisServerHost() {
//        ProxyConfig proxyConfig = AppContext.getBean(ProxyConfig.class);
//        if (proxyConfig != null) {
//            return proxyConfig.getServerHost();
//        }
//        return null;
//    }
//
//    public static Integer getThisServerPort() {
//        ProxyConfig proxyConfig = AppContext.getBean(ProxyConfig.class);
//        if (proxyConfig != null) {
//            return proxyConfig.getServerPort();
//        }
//        return null;
//    }
//
//    public static String getLocalHost() {
//        ProxyConfig proxyConfig = AppContext.getBean(ProxyConfig.class);
//        if (proxyConfig != null) {
//            return proxyConfig.getServiceHost();
//        }
//        return null;
//    }
//
//    public static Integer getLocalPort() {
//        ProxyConfig proxyConfig = AppContext.getBean(ProxyConfig.class);
//        if (proxyConfig != null) {
//            return proxyConfig.getServicePort();
//        }
//        return null;
//    }
//}
