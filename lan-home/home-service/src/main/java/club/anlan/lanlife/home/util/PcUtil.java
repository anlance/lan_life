package club.anlan.lanlife.home.util;

import lombok.extern.slf4j.Slf4j;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2022/8/28 23:54
 */
@Slf4j
public class PcUtil {

    private static final int PORT = 9;
    private static final String header = "FF:FF:FF:FF:FF:FF";
    private static final String concatStr = ":";
    private static final String macStr = "A8:5E:45:2C:3A:84";
    private static final String ipStr = "192.168.0.113";

    public static void start() {
        StringBuilder stringBuilder = new StringBuilder(header);
        for (int i = 0; i < 16; i++) {
            stringBuilder.append(concatStr);
            stringBuilder.append(macStr);
        }

        try {
            byte[] bytes = getHexBytes(stringBuilder.toString());
            InetAddress address = InetAddress.getByName(ipStr);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, PORT);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            socket.close();
            log.info("已发送局域网唤醒数据包");
        } catch (Exception e) {
            log.error("无法发送Lan唤醒数据包: " + e);
        }
    }

    private static byte[] getHexBytes(String macStr) throws IllegalArgumentException {
        String[] hex = macStr.split("(\\:|\\-)");
        byte[] bytes = new byte[hex.length];
        try {
            for (int i = 0; i < hex.length; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("请使用十六进制的字符");
        }
        return bytes;
    }
}
