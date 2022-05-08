package club.anlan.lanlife.auth.service.impl;

import club.anlan.lanlife.auth.component.BasicComponent;
import club.anlan.lanlife.auth.constant.Constant;
import club.anlan.lanlife.auth.dto.UserDto;
import club.anlan.lanlife.auth.redis.RedisKey;
import club.anlan.lanlife.auth.service.UserService;
import club.anlan.lanlife.component.base.enums.ClientType;
import club.anlan.lanlife.component.base.exception.BusinessRuntimeException;
import club.anlan.lanlife.component.redis.cache.TokenUserNameCache;
import club.anlan.lanlife.component.redis.cache.UserSessionCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * UserServiceImpl
 *
 * @author lan
 * @version 1.0
 * @date 2021/10/7 16:29
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BasicComponent basicComponent;

    @Autowired
    private RedisTokenStore redisTokenStore;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

    @Autowired
    private TokenUserNameCache tokenUserNameCache;

    @Autowired
    private UserSessionCache userSessionCache;

    @Value("${security.expired_time.access_token.bs:1800}")
    private long expiredSecond;

    @Value("${security.expired_time.access_token.third:604800}")
    private long thirdExpiredSecond;

    @Override
    public UserDto.User selectByLoginName(String loginName) {
        if (StringUtils.isNotEmpty(loginName)) {
            return basicComponent.getUser(UserDto.Query.valueOf(loginName));
        }
        return null;
    }

    @Override
    public Principal getUser(Principal principal, HttpServletRequest request) {
        if (principal instanceof AbstractAuthenticationToken) {
            OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) ((AbstractAuthenticationToken)principal).getDetails();
            if (details == null) {
                return principal;
            }
            String accessToken = details.getTokenValue();
            // client模式
            if (!(((AbstractAuthenticationToken) principal).getPrincipal() instanceof UserDto.User)) {
                this.refreshToken(accessToken, true);
                return principal;
            }
            OAuth2AccessToken oauth2AccessToken = redisTokenStore.readAccessToken(accessToken);
            if (oauth2AccessToken == null) {
                return principal;
            }
            this.refreshToken(accessToken, false);
        }
        return principal;
    }

    private void refreshToken(String accessToken, boolean clientMode) {
        if (StringUtils.isEmpty(accessToken)) {
            return;
        }
        RedisConnection conn = getConnection();
        try {
            byte[] key = serializeKey(RedisKey.OAUTH2_ACCESS_TOKEN_PREFIX + accessToken);
            byte[] bytes = conn.get(key);
            DefaultOAuth2AccessToken auth2AccessToken = serializationStrategy.deserialize(bytes,
                    DefaultOAuth2AccessToken.class);
            if (auth2AccessToken == null) {
                log.info("auth2AccessToken is null.");
                return;
            }
            if (clientMode) {
                long curExpiredSecond = getExpiredTime(ClientType.THIRD);
                auth2AccessToken.setExpiration(new Date(System.currentTimeMillis() + curExpiredSecond * 1000));
                // 重置access中OAuth2AccessToken过期信息
                conn.set(key, serializationStrategy.serialize(auth2AccessToken));
                // 重置access、auth、uname_to_access的过期时间
                conn.expire(key, curExpiredSecond);
                conn.expire(serializeKey(RedisKey.AUTH_PREFIX + accessToken), curExpiredSecond);
            } else {
                Map<String, Object> userMap = auth2AccessToken.getAdditionalInformation();
                String username = (String) userMap.get("username");
                if (StringUtils.isNotEmpty(username)) {
                    ClientType clientType = null;
                    String[] array = username.split(Constant.DOUBLE_UNDERLINE);
                    if (array.length >= 2) {
                        clientType = ClientType.getEnumDefault(array[1]);
                    }
                    long curExpiredSecond = this.getExpiredTime(clientType);
                    auth2AccessToken.setExpiration(new Date(System.currentTimeMillis() + curExpiredSecond * 1000));
                    // 重置access中OAuth2AccessToken过期信息
                    conn.set(key, serializationStrategy.serialize(auth2AccessToken));
                    // 重置access、auth、usernname_to_access的过期时间
                    conn.expire(key, curExpiredSecond);
                    conn.expire(serializeKey(RedisKey.AUTH_PREFIX + accessToken), curExpiredSecond);
                    conn.expire(serializeKey(RedisKey.OAUTH2_USERNAME_PREFIX + username), curExpiredSecond);
                    tokenUserNameCache.expire(accessToken, curExpiredSecond, TimeUnit.SECONDS);
                    userSessionCache.expire(username, curExpiredSecond, TimeUnit.SECONDS);
                }
            }
        } finally {
            conn.close();
        }
    }

    private RedisConnection getConnection() {
        return redisConnectionFactory.getConnection();
    }

    private byte[] serializeKey(String object) {
        return serializationStrategy.serialize(object);
    }


    @Override
    public long getExpiredTime(ClientType clientType) {
        ClientType curClientType = (clientType != null ? clientType : ClientType.BS);
        long result = expiredSecond;
        switch (curClientType) {
            case BS:
                break;
            case THIRD:
                result = thirdExpiredSecond;
                break;
            default:
                break;
        }
        return result;
    }
}
