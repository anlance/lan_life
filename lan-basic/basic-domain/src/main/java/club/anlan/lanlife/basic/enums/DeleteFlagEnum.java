package club.anlan.lanlife.basic.enums;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/5 11:53
 */
@AllArgsConstructor
public enum  DeleteFlagEnum {

    EXISTS(1,"存在"),
    DELETED(0, "已删除");

    private Integer flag;

    private String desc;

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
