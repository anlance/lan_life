package club.anlan.lanlife.basic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户和菜单关联关系
 *
 * @author lan
 * @version 1.0
 * @date 2022/5/4 0:53
 */
@Data
@TableName("bs_user_menu")
public class UserMenu {

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 菜单编码
     */
    private String menuCode;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
