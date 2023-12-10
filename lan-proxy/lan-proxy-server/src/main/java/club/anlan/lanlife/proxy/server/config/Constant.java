package club.anlan.lanlife.proxy.server.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2023/12/10 20:52
 */
public class Constant {

    public static String getDefaultHttpResponse(Date now) {
        SimpleDateFormat sdf = new SimpleDateFormat("E", Locale.ENGLISH);
        return "HTTP/1.1 500\r\n" +
                "Server: lan-proxy-server\r\n" +
                "Date: " + sdf.format(now) + ",  " + now.toGMTString() + "\r\n" +
                "Content-Type: application/json;charset=UTF-8\r\n" +
                "Transfer-Encoding: chunked\r\n" +
                "Connection: keep-alive\r\n" +
                "\r\n" +
                "5B\r\n" +
                "{\"success\":false,\"code\":\"500\",\"errMsg\":\"no client, check your local server pls.\",\"data\":{}}\r\n0\r\n\r\n";
    }


}
