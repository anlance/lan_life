package club.anlan.lanlife.gateway.prxoy.manager;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 代理客户端与后端真实服务器连接管理
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/17 19:44
 */
@Slf4j
public class ClientChannelManager {

    private static volatile Channel proxyChannel;

    private static Map<String, String> localToRemote = new ConcurrentHashMap<String, String>();

    public static void setProxyChannel(Channel proxyChannel) {
        ClientChannelManager.proxyChannel = proxyChannel;
    }

    public static Channel getProxyChannel() {
        return proxyChannel;
    }

    public static void removeCmdChannel() {
        proxyChannel.close();
        proxyChannel = null;
    }

    public static String getRemoteId(String localId) {
        return localToRemote.get(localId);
    }

    public static void setRemoteId(String localId, String remoteId) {
        localToRemote.put(localId, remoteId);
    }
}

