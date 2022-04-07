package club.anlan.lanlife.auth.service;

import club.anlan.lanlife.auth.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * user service
 *
 * @author lan
 * @version 1.0
 * @date 2021/10/7 16:29
 */
public interface UserService {

    /**
     * 根据登录名获取用户信息
     *
     * @param loginName 登录名
     * @return 用户信息
     */
    UserDto.User selectByLoginName(String loginName);

    /**
     * 获取用户信息
     */
    Principal getUser(Principal principal, HttpServletRequest request);

}
