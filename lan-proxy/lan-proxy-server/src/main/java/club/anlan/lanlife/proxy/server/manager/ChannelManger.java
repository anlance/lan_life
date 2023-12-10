package club.anlan.lanlife.proxy.server.manager;

import club.anlan.lanlife.component.utils.collection.CollectionUtil;
import club.anlan.lanlife.proxy.server.util.ChannelUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ChannelManger
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/17 16:41
 */
@Slf4j
public class ChannelManger {

    /**
     * 代理通道绑定的客户端唯一标志
     */
    private static final AttributeKey<String> CLIENT_KEY = AttributeKey.newInstance("client_key");

    /**
     * 代理通道绑定的客户端IP
     */
    private static final AttributeKey<String> CLIENT_IP = AttributeKey.newInstance("client_ip");

    /**
     * 代理通道绑定的客户端端口
     */
    private static final AttributeKey<Integer> CLIENT_PORT = AttributeKey.newInstance("client_port");

    /**
     * key: clientKey, value: cmd channel
     * todo 目前只有一个代理通道，后续可通过线程池来扩展该部分
     */
    private static Map<String, Channel> cmdChannels = new ConcurrentHashMap<String, Channel>(1);

    /**
     * key: requestId, value: user channel
     */
    private static Map<String, Channel> userChannels = new ConcurrentHashMap<String, Channel>(256);

    /**
     * 添加代理 channel
     *
     * @param ctx 通道上下文
     */
    public static void addCmdChannel(String clientKey, ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        channel.attr(CLIENT_KEY).set(clientKey);
        channel.attr(CLIENT_IP).set(ChannelUtil.getIp(ctx));
        channel.attr(CLIENT_PORT).set(ChannelUtil.getPort(ctx));
        cmdChannels.put(clientKey, channel);
    }

    /**
     * 代理客户端连接断开后清除关系
     *
     * @param channel channel
     */
    public static void removeCmdChannel(Channel channel) {
        log.warn("channel closed, clear user channels, {}", channel);
        removeCmdChannel();
    }

    public static Channel getCmdChannel() {
        if (CollectionUtil.isEmpty(cmdChannels.values())) {
            return null;
        }
        return cmdChannels.values().iterator().next();
    }

    public static Channel getCmdChannel(String clientKey) {
        return cmdChannels.get(clientKey);
    }

    public static void removeCmdChannel() {
        Channel cmdChannel = getCmdChannel();
        String clientKey = cmdChannel.attr(CLIENT_KEY).get();
        cmdChannels.remove(clientKey);
        cmdChannel.close();
    }


    public static void addUserChannel(String requestId, Channel channel) {
        userChannels.put(requestId, channel);
    }

    public static Channel getUserChannel(String requestId) {
        return userChannels.get(requestId);
    }

    public static void removeUserChannel(String requestId) {
        userChannels.remove(requestId);
    }
}

