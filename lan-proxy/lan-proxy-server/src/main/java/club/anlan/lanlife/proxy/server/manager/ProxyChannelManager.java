package club.anlan.lanlife.proxy.server.manager;

import club.anlan.lanlife.proxy.server.config.ProxyConfig;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ProxyChannelManager
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/17 16:41
 */
@Slf4j
public class ProxyChannelManager {

    private static final AttributeKey<Integer> CHANNEL_PORT = AttributeKey.newInstance("channel_port");

    private static final AttributeKey<String> CHANNEL_CLIENT_KEY = AttributeKey.newInstance("channel_client_key");

    private static Map<Integer, Channel> portCmdChannelMapping = new ConcurrentHashMap<Integer, Channel>();

    /**
     * key: clientKey, value: channel
     */
    private static Map<String, Channel> cmdChannels = new ConcurrentHashMap<String, Channel>();

    private static Map<String, Channel> userChannels = new ConcurrentHashMap<String, Channel>();

    /**
     * 增加代理服务器端口与代理控制客户端连接的映射关系
     *
     * @param port    端口
     * @param channel channel
     */
    public static void addCmdChannel(Integer port, String clientKey, Channel channel) {

        if (port == null) {
            throw new IllegalArgumentException("port can not be null");
        }

        // 当前只有一个客户端
        portCmdChannelMapping.put(port, channel);
        channel.attr(CHANNEL_PORT).set(port);
        channel.attr(CHANNEL_CLIENT_KEY).set(clientKey);
        cmdChannels.put(clientKey, channel);
    }

    /**
     * 代理客户端连接断开后清除关系
     *
     * @param channel channel
     */
    public static void removeCmdChannel(Channel channel) {
        log.warn("channel closed, clear user channels, {}", channel);
        if (channel.attr(CHANNEL_PORT).get() == null) {
            return;
        }

        String clientKey = channel.attr(CHANNEL_CLIENT_KEY).get();
        Channel channel0 = cmdChannels.remove(clientKey);
        if (channel != channel0) {
            cmdChannels.put(clientKey, channel);
        }

        Integer port = channel.attr(CHANNEL_PORT).get();
        Channel proxyChannel = portCmdChannelMapping.remove(port);

        // 在执行断连之前新的连接已经连上来了
        if (proxyChannel != channel) {
            portCmdChannelMapping.put(port, proxyChannel);
        }

        if (channel.isActive()) {
            log.info("disconnect proxy channel {}", channel);
            channel.close();
        }

    }

    public static Channel getCmdChannel() {
        return portCmdChannelMapping.get(ProxyConfig.getServerPort());
    }

    public static Channel getCmdChannel(String clientKey) {
        return cmdChannels.get(clientKey);
    }


    public static void addUserChannel(String userId, Channel channel) {
        userChannels.put(userId, channel);
    }

    public static Channel getUserChannel(String userId) {
        return userChannels.get(userId);
    }

    public static void removeUserChannel(String userId) {
        userChannels.remove(userId);
    }
}

