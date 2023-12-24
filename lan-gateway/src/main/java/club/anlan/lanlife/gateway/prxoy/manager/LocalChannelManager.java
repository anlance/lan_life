package club.anlan.lanlife.gateway.prxoy.manager;

import club.anlan.lanlife.component.utils.StringUtil;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 代理客户端与后端真实服务器连接管理
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/17 19:44
 */
public class LocalChannelManager {


    private static Map<String, Channel> remoteToLocal = new ConcurrentHashMap<String, Channel>();
    private static Map<String, String> channelIdToRequestId = new ConcurrentHashMap<String, String>();
    private static Map<String, String> requestIdToChannelId = new ConcurrentHashMap<String, String>();

    public static void setLocalChannel(String requestId, Channel localChannel) {
        remoteToLocal.put(requestId, localChannel);
        String channelId = localChannel.id().asLongText();
        channelIdToRequestId.put(channelId, requestId);
        requestIdToChannelId.put(requestId, channelId);
    }

    public static Channel getLocalChannel(String requestId) {
        return remoteToLocal.get(requestId);
    }

    /**
     * 清除
     */
    public static void removeLocalChannel(String requestId) {
        Channel localChannel = remoteToLocal.get(requestId);
        if (localChannel != null) {
            localChannel.close();
            localChannel = null;
        }
        remoteToLocal.remove(requestId);
        String channelId = requestIdToChannelId.get(requestId);
        if (StringUtil.isNotEmpty(channelId)) {
            channelIdToRequestId.remove(channelId);
        }
        requestIdToChannelId.remove(requestId);
    }

    public static void removeLocalChannel(Channel channel) {
        if (channel == null) {
            return;
        }
        String channelId = channel.id().asLongText();
        String requestId = channelIdToRequestId.get(channelId);
        if (StringUtil.isNotEmpty(requestId)) {
            Channel localChannel = remoteToLocal.get(requestId);
            if (localChannel != null) {
                localChannel.close();
                localChannel = null;
            }
            remoteToLocal.remove(requestId);
            requestIdToChannelId.remove(requestId);

        }
        channelIdToRequestId.remove(channelId);
    }

}

