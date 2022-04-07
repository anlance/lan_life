package club.anlan.lanlife.component.base.enums;

import lombok.Getter;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2021/5/4 13:31
 */
@Getter
public enum BusinessType {

    QUERY("查询"),
    INSERT("新增"),
    UPDATE("修改"),
    DELETE("删除"),
    EXPORT("导出"),
    IMPORT("导入"),
    ;

    String description;

    BusinessType(String description) {
        this.description = description;
    }
}
