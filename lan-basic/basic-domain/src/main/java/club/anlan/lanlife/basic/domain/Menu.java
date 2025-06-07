//package club.anlan.lanlife.basic.domain;
//
//import com.baomidou.mybatisplus.annotation.*;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 菜单
// *
// * @author lan
// * @version 1.0
// * @date 2022/5/4 0:52
// */
//@Data
//@NoArgsConstructor
//@TableName("bs_menu")
//public class Menu {
//
//    @TableId(type = IdType.ASSIGN_UUID)
//    private String id;
//
//    private String menuCode;
//
//    private String menuName;
//
//    @TableField("menu_icon")
//    private String menuIcon;
//
//    @TableField("menu_path")
//    private String menuPath;
//
//    /**
//     * 是否是最外层菜单
//     */
//    private Integer isMenu;
//
//    /**
//     * 是否展示
//     */
//    private Integer isShow;
//
//    /**
//     * 排序
//     */
//    private Long sort;
//
//    private LocalDateTime createTime;
//
//    private LocalDateTime updateTime;
//}
