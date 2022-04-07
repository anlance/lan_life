package club.anlan.lanlife.component.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取客户端IP工具类
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/8 0:36
 */
public class RemoteAddrUtil {

    public static final String REMOTE_ADD_HEADER = "X-Real-IP";

    /**
     * 获取客户端IP
     */
    public static String getRemoteAddr() {
        HttpServletRequest request = getHttpRequest();
        return getRemoteAddr(request);
    }

    /**
     * 获取客户端IP
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return request.getHeader(REMOTE_ADD_HEADER);
    }

    /**
     * 本次请求的HttpServletRequest
     */
    public static HttpServletRequest getHttpRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return requestAttributes.getRequest();
    }
}
