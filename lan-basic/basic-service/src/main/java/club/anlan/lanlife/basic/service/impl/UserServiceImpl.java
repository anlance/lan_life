package club.anlan.lanlife.basic.service.impl;

import club.anlan.lanlife.basic.domain.User;
import club.anlan.lanlife.basic.dto.UserDto;
import club.anlan.lanlife.basic.mapper.UserMapper;
import club.anlan.lanlife.basic.redis.RedisKey;
import club.anlan.lanlife.basic.service.UserService;
import club.anlan.lanlife.basic.vo.SaltVo;
import club.anlan.lanlife.component.redis.util.RedisUtil;
import club.anlan.lanlife.component.utils.StringUtil;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 用户
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/3 20:46
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Integer RANDOM_CODE_NUM = 10;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void saveOrUpdate(User user) {
        if (StringUtil.isNotEmpty(user.getId())) {
            userMapper.updateById(user);
        } else {
            userMapper.insert(user);
        }
    }

    @Override
    public UserDto.User getUser(UserDto.Query query) {
        if (Objects.nonNull(query) && StringUtil.isNotEmpty(query.getLoginName())) {
            return userMapper.getUser(query);
        }
        return null;
    }


    @Override
    public SaltVo getSalt(String username) {
        if (StringUtil.isNotEmpty(username)) {
            User user = new LambdaQueryChainWrapper<>(userMapper)
                    .eq(User::getLoginName, username)
                    .select(User::getSalt)
                    .one();

            if (Objects.nonNull(user) && StringUtil.isNotEmpty(user.getSalt())) {
                String randomCode = StringUtil.generateRandomChars(RANDOM_CODE_NUM);
                redisUtil.add(RedisKey.AUTH_LOGIN_USERNAME_RANDOM_CODE + username, randomCode, 300, TimeUnit.SECONDS);
                return SaltVo.valueOf(user.getSalt(), randomCode);
            }
        }
        return SaltVo.defaultVo();
    }
}
