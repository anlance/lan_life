package club.anlan.lanlife.basic.service;

import club.anlan.lanlife.basic.domain.User;
import club.anlan.lanlife.basic.dto.UserDto;
import club.anlan.lanlife.basic.vo.SaltVo;

/**
 * 用户
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/3 20:46
 */
public interface UserService {

    /**
     * 插入或者更新用户
     *
     * @param user 用户
     */
    void saveOrUpdate(User user);

    /**
     * 查询用户
     *
     * @param query 查询条件
     * @return 用户信息
     */
    UserDto.User getUser(UserDto.Query query);

    /**
     * 获取盐
     *
     * @param username 用户名
     * @return 盐和随机数
     */
    SaltVo getSalt(String username);
}
