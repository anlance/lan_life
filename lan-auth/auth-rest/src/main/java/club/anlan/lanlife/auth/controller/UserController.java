package club.anlan.lanlife.auth.controller;

import club.anlan.lanlife.auth.service.UserService;
import club.anlan.lanlife.component.base.annotation.Log;
import club.anlan.lanlife.component.base.result.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * user 控制器
 *
 * @author lan
 * @version 1.0
 * @date 2021/10/7 16:37
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Log(description = "获取登录信息")
    @GetMapping("/current")
    public Principal current(Principal principal, HttpServletRequest request) {
        return userService.getUser(principal, request);
    }

}

