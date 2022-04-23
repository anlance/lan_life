package club.anlan.lanlife.gateway.prxoy.manager;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 代理客户端与后端真实服务器连接管理
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/17 19:44
 */
@Slf4j
public class ClientChannelManager {


    private static final int MAX_POOL_SIZE = 100;

    private static volatile Channel cmdChannel;

    private static Map<String, String> localToRemote = new ConcurrentHashMap<String, String>();

    private static Map<String, Channel> remoteToLocalChannel = new ConcurrentHashMap<String, Channel>();


    public static void removeLocalChanel(String uri) {
        remoteToLocalChannel.remove(uri);
        Channel localChannel = getLocalChannel(uri);
        if (Objects.nonNull(localChannel)) {
            localChannel.close();
        }
    }

//    public static void addLocalChannel(String localId, Channel channel) {
//        localChannels.put(localId, channel);
//    }

//    public static Channel getLocalChannel(String localId) {
//        return localChannels.get(localId);
//    }

    public static void setCmdChannel(Channel cmdChannel) {
        ClientChannelManager.cmdChannel = cmdChannel;
    }

    public static Channel getCmdChannel() {
        return cmdChannel;
    }

    public static void setLocalChannel(String uri, Channel localChannel) {
        remoteToLocalChannel.put(uri, localChannel);
    }

    public static Channel getLocalChannel(String uri) {
        return remoteToLocalChannel.get(uri);
    }

    public static void removeLocalChannel(String uri) {
        remoteToLocalChannel.remove(uri);
    }

    public static String getRemoteId(String localId) {
        return localToRemote.get(localId);
    }

    public static void setRemoteId(String localId, String remoteId) {
        localToRemote.put(localId, remoteId);
    }

    public static void removeRemoteId(String localId){
        localToRemote.remove(localId);
    }


//    public void channelInactive(ChannelHandlerContext ctx) {
//        reconnectWait();
//        connectProxyServer();
//    }
}

