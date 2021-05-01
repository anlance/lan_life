package club.anlan.lanlife.base.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/24 13:52
 */
@Slf4j
public class IPUtil {

    private final static String UNKNOWN = "unknown";

    /** ip V4 */
    private final static String REG_EXP_IP = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

    // 获取ip的shell命令，因后续需要使用MessageFormat.format进行格式化，对其中的大括号和单引号都使用单引号'进行了转义
    private final static String CMD_GET_HOST_IP = "ifconfig {0} | grep \"inet addr\" | awk ''$1==\"inet\" '{print $2'}'' | awk -F: '''{print $2'}''";

    /**
     * 从request获取请求IP
     * @param request
     * @return
     */
    public static String getRemoteHost(HttpServletRequest request){
        String ip = request.getHeader("x-real-ip");
        if(StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)){
            ip = request.getHeader("x-forwarded-for");
        }
        if(StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        return StringUtils.equals("0:0:0:0:0:0:0:1", ip) ? "127.0.0.1" : ip;
    }

    /**
     * 判断ip/域名是否通
     * @param ip
     * @return
     */
    public static boolean isReachable(String ip) {
        boolean flag = false;
        try {
            InetAddress address = InetAddress.getByName(ip);
            log.debug("addr {}", ip);
            flag = address.isReachable(3000);
            log.debug("addr {}, isReachable: {}",ip, flag);
        } catch (Exception e) {
            log.error("Unknown host: {}", ip);
        }
        return flag;
    }

    /**
     * 判断端口是否通
     * @param ip
     * @param port
     * @return
     */
    public static boolean isReachable(String ip, String port) {
        boolean flag = false;
        Socket socket = null;
        try {
            socket = new Socket(ip, Integer.parseInt(port));
            log.debug("addr {}, isReachable: {}",ip, flag);
            flag = true;
        } catch (Exception e) {
            log.error("Unknown host: {}", ip);
        } finally {
            try {
                if(socket != null){
                    socket.close();
                }
            } catch (IOException e) {
                log.error("exception : {}", e);
            }
        }
        return flag;
    }
    /**
     * 将IP转化为数字，转化格式为 各片段三位填充数字相拼接(相应位数如果没有，则用0填充)
     * 如 1.2.3.5 拼接完后为 "001002003005"，用于ip起始查询
     */
    public static String toIpNumberStr(String ip) {
        String result = null;
        if (StringUtils.isNotEmpty(ip)) {
            StringBuilder ipNumStrBuilder = new StringBuilder();
            String[] ipSegments = ip.split("\\.");
            Format decimalFormat = new DecimalFormat("000");
            try {
                for (String ipSeg : ipSegments) {
                    Integer ipSegInt = Integer.valueOf(ipSeg);
                    ipNumStrBuilder.append(decimalFormat.format(ipSegInt));
                }
            } catch (Exception e) {
                log.error("exception : {}", e);
            }
            result = ipNumStrBuilder.toString();
        }
        return result;
    }

    /**
     * 根据机器ip获取工作进程编号
     * 例如：机器的IP为192.168.1.108 截取最后10位 01 01101100 转为10进制364 设置工作进程编号364
     */
    public static long geneteWorkerId() {
        InetAddress addRess = null;
        try {
            addRess = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Cannot get LocalHost InetAddress, please check your network!");
        }
        byte[] ipAddressByte = addRess.getAddress();
        return ((ipAddressByte[ipAddressByte.length -2] & 0B11) << Byte.SIZE)
                + (ipAddressByte[ipAddressByte.length - 1]&0xFF);

    }

    /**
     * 将 ip 地址转成 long 型数值
     * <br><br>
     * 将IP地址转化成整数的方法如下：<br>
     * 1、如ip为A.B.C.D，通过String的split方法按.分隔得到长度为4的数组 <br>
     * 2、方法1，通过分段权值倍数累加，每段的权值分别为 256^0 / 256^1 / 256^2 / 256^3 <br>
     * 3、方法2，通过左移位操作（<<）给每一段的数字加权，A的权为1，B的权为2的8次方，C的权为2的16次方，D的权为2的24次方
     * @param ip 待处理的源ip字符串
     * @return 转换后的long值，如传入的ip不合法，则直接返回0
     */
    public static long convert2Long(String ip) {
        long result = 0;

        // 检测是否为合法ip
        Matcher matcher = Pattern.compile(REG_EXP_IP).matcher(ip);
        if (matcher.matches()) {  // 合法ip
            String[] ipSegments = ip.split("\\.");

            for (int i = 0; i < ipSegments.length; i++) {
                int power = i;
                long segValue = Long.parseLong(ipSegments[i]);

                // 以2^8即256作为基数（底数），各个部分的权值分别是 256^0 / 256^1 / 256^2 / 256^3
                result += segValue * Math.pow(256, power);
                // 或者
                //result += segValue << (8 * power);
            }
        }

        return result;
    }

    /**
     * 将 long 值转换成实际 ip 字符串
     * <br><br>
     * 1、将long值value与oxff按位与，得到ip的第一段（从右往左）
     * 2、插入间隔符 ‘.’
     * 3、将当前value值按位右移8位（一个ip分段的二进制长度），value被置成新值
     * 4、循环以上步骤1、2、3，直到4个ip分段被处理完
     * @param value 待转换 long 值
     * @return 转换后的实际 ip
     */
    public static String convert2Ip(long value) {
        // 存储转换后的ip串
        StringBuilder result = new StringBuilder(15);

        for (int i = 0; i < 4; i++) {
            result.append(Long.toString(value & 0xff));
            if (i < 3) {
                result.append(".");
            }
            value = value >> 8;
        }
        return result.toString();
    }

    /**
     * 将ip字符串转换成十六进制的8位字符串
     * @param ip 待转换ip
     * @param isLowerCase 是否采用十六进制小写，默认小写
     * @param fixedLength 采用固定长度，如为null，则默认为8位，若实际长度没有达到固定长度则左侧补0，一旦超过指定的固定长度，则以实际为准
     * @return 转换后的十六进制字符串，如相应的十六进制不足8位，则左侧高位补0
     */
    public static String convert2Hex(String ip, boolean isLowerCase, Integer fixedLength) {
        // 结果字符串
        String hexResultString = "";
        // 结果串长度默认为8
        fixedLength = fixedLength == null ? 8 : fixedLength;
        long longValue = convert2Long(ip);
        String hexString = Long.toHexString(longValue);

        if (!StringUtils.isEmpty(hexString) && hexString.length() < fixedLength) {
            while (hexString.length() < 8) {
                hexString = "0" + hexString;
            }
        }
        if (isLowerCase) {
            hexResultString = hexString.toLowerCase();
        } else {
            hexResultString = hexString.toUpperCase();
        }
        return hexResultString;
    }

    /**
     * 将1-8位的十六进制格式的字符串转换成真实ip
     * @param stringHex 8位以内的十六进制数值对应的字符串
     * @return 真实ip，如传入的十六进制格式字符串不合法，则直接返回空字符串""
     */
    public static String convert2Ip(String stringHex) {
        String regex = "^[A-Fa-f0-9]{1,8}$";
        if (!stringHex.matches(regex)) { // 格式要求1-8位十六进制字符串，如不合法，直接返回空
            return "";
        }
        long value = Long.parseLong(stringHex, 16);
        return convert2Ip(value);
    }

}

