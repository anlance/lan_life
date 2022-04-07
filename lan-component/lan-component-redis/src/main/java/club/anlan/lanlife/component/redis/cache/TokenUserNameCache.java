package club.anlan.lanlife.component.redis.cache;

import club.anlan.lanlife.component.base.enums.ClientType;
import club.anlan.lanlife.component.redis.base.AbstractRedisCache;
import org.springframework.stereotype.Component;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/5 20:53
 */
@Component
public class TokenUserNameCache extends AbstractRedisCache<String, String> {

    public static final String KEY_TOKEN_USERNAME_PRE = "oauth:session:token_username:%s";

    public static final String KEY_TEMPLATE_EXT = "%s__%s__%s";

    /**
     * 生成value信息
     *
     * @date 2019年10月14日 下午5:06:40
     * @author 27477
     * @param username
     * @param clientType
     * @param companyId
     * @return
     */
    public String createValue(String username, ClientType clientType, String companyId) {
        return String.format(KEY_TEMPLATE_EXT, username,
                clientType != null ? clientType.getCode() : ClientType.BS.getCode(), companyId);
    }

    @Override
    public String getKey(String key) {
        return String.format(KEY_TOKEN_USERNAME_PRE, key);
    }

}

