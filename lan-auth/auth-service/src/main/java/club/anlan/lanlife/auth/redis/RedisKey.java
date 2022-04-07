package club.anlan.lanlife.auth.redis;

/**
 * 缓存 key
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/5 13:45
 */
public class RedisKey {

    /**
     * 登录时的随机数缓存
     */
    public static final String AUTH_LOGIN_USERNAME_RANDOM_CODE = "auth:login:username:random:code:";


    public static final String AUTH_PREFIX = "auth:";

    /**
     * Oauth2中存放access_token到redis中的前缀
     */
    public static final String OAUTH2_ACCESS_TOKEN_PREFIX = "access:";

    /**
     * Oauth2中存放uname_to_access:browser到redis中的前缀
     */
    public static final String OAUTH2_USERNAME_PREFIX = "uname_to_access:browser:";
}
