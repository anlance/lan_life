package club.anlan.lanlife.auth.security.encoder;

import club.anlan.lanlife.auth.redis.RedisKey;
import club.anlan.lanlife.component.base.spring.AppContext;
import club.anlan.lanlife.component.redis.util.RedisUtil;
import club.anlan.lanlife.component.utils.StringUtil;
import club.anlan.lanlife.component.utils.security.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 自定义密码加密
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/4 14:38
 */
@Slf4j
public class CustomPasswordEncoder extends BCryptPasswordEncoder implements PasswordEncoder {

    @Override
    public boolean isPasswordValid(String encPass, String rawPass, String username) {
        if (StringUtil.isNotEmpty(encPass) && StringUtil.isNotEmpty(rawPass)) {
            RedisUtil redisUtil = AppContext.getBean(RedisUtil.class);
            String random = redisUtil.get(RedisKey.AUTH_LOGIN_USERNAME_RANDOM_CODE + username);
            if (StringUtil.isNotEmpty(random)) {
                return encPass.equals(MD5Util.md5Hex(rawPass + random));
            }
        }
        return false;
    }
}
