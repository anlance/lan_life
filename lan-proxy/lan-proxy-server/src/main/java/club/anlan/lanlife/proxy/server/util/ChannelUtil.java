package club.anlan.lanlife.proxy.server.util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

import java.net.InetSocketAddress;

/**
 * channel util
 *
 * @author lan
 * @version 1.0
 * @date 2023/7/28 22:16
 */
@Data
public class ChannelUtil {

    /**
     * 获取通道 ID
     */
    public static String getChannelId(ChannelHandlerContext ctx) {
        return ctx.channel().id().asLongText();
    }

    /**
     * 获取IP
     */
    public static String getIp(ChannelHandlerContext ctx) {
        InetSocketAddress inSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        if (inSocket != null) {
            return inSocket.getAddress().getHostAddress();
        }
        return null;
    }

    /**
     * 获取端口
     */
    public static int getPort(ChannelHandlerContext ctx) {
        InetSocketAddress inSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        if (inSocket != null) {
            return inSocket.getPort();
        }
        return 0;
    }

    /**
     * 获取IP
     */
    public static String getIp(Channel channel) {
        InetSocketAddress inSocket = (InetSocketAddress) channel.remoteAddress();
        if (inSocket != null) {
            return inSocket.getAddress().getHostAddress();
        }
        return null;
    }

    /**
     * 获取端口
     */
    public static int getPort(Channel channel) {
        InetSocketAddress inSocket = (InetSocketAddress) channel.remoteAddress();
        if (inSocket != null) {
            return inSocket.getPort();
        }
        return 0;
    }
}
