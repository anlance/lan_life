package club.anlan.lanlife.component.redis.util;

import club.anlan.lanlife.component.base.spring.AppContext;
import club.anlan.lanlife.component.redis.cache.TokenUserNameCache;
import club.anlan.lanlife.component.redis.cache.UserSessionCache;
import club.anlan.lanlife.component.redis.cache.UserSessionInfo;
import club.anlan.lanlife.component.utils.RemoteAddrUtil;
import club.anlan.lanlife.component.utils.StringUtil;
import club.anlan.lanlife.component.utils.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * session信息工具类
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/8 0:30
 */
@Slf4j
public class UserSessionUtil {

    private static UserSessionCache userSessionCache = AppContext.getBean(UserSessionCache.class);

    private static TokenUserNameCache tokenUserNameCache = AppContext.getBean(TokenUserNameCache.class);

    public static UserSessionInfo getUserSessionInfo() {
        HttpServletRequest request = RemoteAddrUtil.getHttpRequest();
        if (request == null) {
            return null;
        }
        return getUserSessionInfo(request);
    }

    public static String getAccessToken() {
        HttpServletRequest request = RemoteAddrUtil.getHttpRequest();
        if (request != null) {
            return getAccessToken(request);
        }
        return null;
    }

    public static String getAuthorizationInfo() {
        HttpServletRequest request = RemoteAddrUtil.getHttpRequest();
        if (request != null) {
            return getAuthorizationInfo(request);
        }
        return null;
    }

    public static UserSessionInfo getUserSessionInfo(HttpServletRequest request) {
        String xRealIp = RemoteAddrUtil.getRemoteAddr(request);
        UserSessionInfo userSessionInfo = (UserSessionInfo) request.getAttribute(Constants.SESSION_USER_INFO);
        if (userSessionInfo != null) {
            log.info("userSessionInfo: {},X-Real-IP: {}", userSessionInfo, xRealIp);
            return userSessionInfo;
        }

        String accessToken = getAccessToken(request);
        log.info("accessToken: {},X-Real-IP: {}", accessToken, xRealIp);
        if (StringUtil.isEmpty(accessToken)) {
            return null;
        }

        String userName = UserSessionUtil.getUserNameByToken(accessToken);
        if (StringUtil.isEmpty(userName)) {
            return null;
        }
        userSessionInfo = userSessionCache.get(userName);
        log.info("userSessionInfo: {}", userSessionInfo);
        return userSessionInfo;
    }

    public static void updateUserSessionInfo(HttpServletRequest request, UserSessionInfo userSessionInfo,
                                             long expiredSeconds) {
        log.info("updateUserSessionInfo, userSessionInfo: {}", userSessionInfo);
        String accessToken = getAccessToken(request);
        String userName = UserSessionUtil.getUserNameByToken(accessToken);
        if (StringUtil.isEmpty(userName)) {
            log.warn("updateUserSessionInfo failed.");
        }
        userSessionCache.add(userName, userSessionInfo, expiredSeconds, TimeUnit.SECONDS);
    }


    public static String getUserNameByToken(String accessToken) {
        return tokenUserNameCache.get(accessToken);
    }

    public static UserSessionInfo getUserInfoByToken(String accessToken) {
        String userNameExt = getUserNameByToken(accessToken);
        if (StringUtil.isNotEmpty(userNameExt)) {
            return userSessionCache.get(userNameExt);
        }
        return null;
    }

    private static String getAuthorizationInfo(HttpServletRequest request) {
        return request.getHeader(Constants.AUTHORIZATION);
    }

    private static String getAccessToken(HttpServletRequest request) {
        String authorization = getAuthorizationInfo(request);
        log.info("getAccessToken, Authorization: {}", authorization);
        int bearerLength = Constants.BEARER.length() + 1;
        if (StringUtil.isEmpty(authorization) || authorization.length() <= bearerLength) {
            return null;
        }
        String token = authorization.substring(bearerLength);
        if (StringUtil.isEmpty(token)) {
            return null;
        }
        if (StringUtil.equalsIgnoreCase(Constants.NULL, token)) {
            return null;
        }
        return token;
    }
}
