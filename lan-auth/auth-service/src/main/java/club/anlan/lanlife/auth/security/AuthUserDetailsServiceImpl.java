package club.anlan.lanlife.auth.security;

import club.anlan.lanlife.auth.service.UserService;
import club.anlan.lanlife.component.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 认证用户信息
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/4 14:21
 */
@Slf4j
@Service
public class AuthUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtil.isEmpty(username)) {
            log.error("Username is null.");
            return null;
        }
        return userService.selectByLoginName(username);
    }
}
