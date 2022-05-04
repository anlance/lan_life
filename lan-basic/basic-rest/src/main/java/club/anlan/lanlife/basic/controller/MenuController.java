package club.anlan.lanlife.basic.controller;

import club.anlan.lanlife.basic.service.MenuService;
import club.anlan.lanlife.component.base.annotation.Log;
import club.anlan.lanlife.component.base.result.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜单
 *
 * @author lan
 * @version 1.0
 * @date 2022/5/4 9:43
 */
@RestController
@Slf4j
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Log(description = "获取菜单")
    @GetMapping("/list")
    public ResultMessage list() {
        return ResultMessage.createSuccessResult(menuService.listUserMenu());
    }
}
