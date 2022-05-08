package club.anlan.lanlife.basic.service;

import club.anlan.lanlife.basic.vo.MenuVo;

import java.util.List;

/**
 * 菜单
 *
 * @author lan
 * @version 1.0
 * @date 2022/5/4 9:45
 */
public interface MenuService {

    /**
     * 获取用户的菜单列表
     */
    List<MenuVo> listUserMenu();
}
