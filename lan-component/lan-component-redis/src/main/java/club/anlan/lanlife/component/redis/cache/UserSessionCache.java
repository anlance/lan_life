package club.anlan.lanlife.component.redis.cache;

import club.anlan.lanlife.component.base.enums.ClientType;
import club.anlan.lanlife.component.redis.base.AbstractRedisCache;
import org.springframework.stereotype.Component;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/5 22:47
 */
@Component
public class UserSessionCache extends AbstractRedisCache<String, UserSessionInfo> {

    /**
     * user session redis key
     */
    public static final String KEY_MEMBER_USER_STATUS_KET_PRE = "oauth:session:usersession:%s";

    /**
     * 管理用户后缀
     */
    private static final String MANAGER_SUFFIX = "_manager";

    @Override
    public String getKey(String username) {
        return String.format(KEY_MEMBER_USER_STATUS_KET_PRE, username);
    }


}