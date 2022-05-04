package club.anlan.lanlife.basic.vo;

import club.anlan.lanlife.basic.domain.Menu;
import lombok.Data;

/**
 * 菜单vo
 *
 * @author lan
 * @version 1.0
 * @date 2022/5/4 9:48
 */
@Data
public class MenuVo {

    /**
     * react list key
     */
    private String key;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 菜单地址
     */
    private String url;

    /**
     * 菜单名
     */
    private String name;

    /**
     * 排序
     */
    private Long sort;


    public static MenuVo of(Menu menu) {
        MenuVo vo = new MenuVo();
        vo.setKey(menu.getId());
        vo.setIcon(menu.getMenuIcon());
        vo.setName(menu.getMenuName());
        vo.setUrl(menu.getMenuPath());
        vo.setSort(menu.getSort());
        return vo;
    }
}
