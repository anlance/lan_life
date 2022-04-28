package club.anlan.lanlife.commponent.netty.message;

import lombok.Data;

import java.util.Arrays;

/**
 * 代理客户端与代理服务器消息交换协议
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/17 16:05
 */
@Data
public class ProxyMessage {


    /**
     * 认证消息，检测clientKey是否正确
     */
    public static final byte C_TYPE_AUTH = 0x01;

    /**
     * 认证失败
     */
    public static final byte C_TYPE_AUTH_FAILED = 0X02;

    /**
     * 认证成功
     */
    public static final byte C_TYPE_AUTH_SUCCESS = 0X03;

    /**
     * 代理后端服务器建立连接消息
     */
    public static final byte TYPE_CONNECT = 0x04;

    /**
     * 代理后端服务器断开连接消息
     */
    public static final byte TYPE_DISCONNECT = 0x05;

    /**
     * 代理数据传输
     */
    public static final byte P_TYPE_TRANSFER = 0x06;

    /**
     * 用户与代理服务器以及代理客户端与真实服务器连接是否可写状态同步
     */
    public static final byte C_TYPE_WRITE_CONTROL = 0x07;

    /**
     * 心跳消息
     */
    public static final byte TYPE_HEARTBEAT = 0x08;


    /**
     * 消息类型
     */
    private byte type;

    /**
     * 消息流水号
     */
    private long serialNumber;

    /**
     * 消息命令请求信息，请求哪一个客户端，目前只有一个客户端
     */
    private String uri;

    /**
     * 消息传输数据
     */
    private byte[] data;

    /**
     * 认证失败消息
     *
     * @param data 认证失败原因
     */
    public static ProxyMessage authFailedMessage(byte[] data) {
        ProxyMessage message = new ProxyMessage();
        message.setType(C_TYPE_AUTH_FAILED);
        message.setData(data);
        return message;
    }

    /**
     * 认证成功消息
     *
     * @param data 认证成功返回数据
     */
    public static ProxyMessage authSuccessMessage(byte[] data) {
        ProxyMessage message = new ProxyMessage();
        message.setType(C_TYPE_AUTH_SUCCESS);
        message.setData(data);
        return message;
    }

    /**
     * 传输数据
     */
    public static ProxyMessage transferMessage(String uri, byte[] data) {
        ProxyMessage message = new ProxyMessage();
        message.setType(P_TYPE_TRANSFER);
        message.setData(data);
        message.setUri(uri);
        return message;
    }

    /**
     * 关闭连接
     */
    public static ProxyMessage disConnectMessage(String uri) {
        ProxyMessage message = new ProxyMessage();
        message.setType(TYPE_DISCONNECT);
        message.setData("连接已断开".getBytes());
        message.setUri(uri);
        return message;
    }

    /**
     * 建立连接
     */
    public static ProxyMessage connectMessage(String uri) {
        ProxyMessage message = new ProxyMessage();
        message.setType(TYPE_CONNECT);
        message.setData("建立连接".getBytes());
        message.setUri(uri);
        return message;
    }

    @Override
    public String toString() {
        return "ProxyMessage{" +
                "type=" + type +
                ", serialNumber=" + serialNumber +
                ", uri='" + uri + '\'' +
                '}';
    }
}
