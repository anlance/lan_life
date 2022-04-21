package club.anlan.lanlife.gateway.prxoy.manager;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
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

//    private static Map<String, Channel> localChannels = new ConcurrentHashMap<String, Channel>();

    private static volatile Channel cmdChannel;

    private static volatile Channel localChannel;

    private static Map<String, String> localToRemote = new ConcurrentHashMap<String, String>();


//    public static void removeLocalChanel(String localId) {
//        localChannels.remove(localId);
//    }

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

    public static void setLocalChannel(Channel localChannel) {
        ClientChannelManager.localChannel = localChannel;
    }

    public static Channel getLocalChannel() {
        return localChannel;
    }

    public static String getRemoteId(String localId) {
        return localToRemote.get(localId);
    }

    public static void setRemoteId(String localId, String remoteId) {
        localToRemote.put(localId, remoteId);
    }


//    public void channelInactive(ChannelHandlerContext ctx) {
//        reconnectWait();
//        connectProxyServer();
//    }
}

