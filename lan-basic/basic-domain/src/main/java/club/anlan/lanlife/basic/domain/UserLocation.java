package club.anlan.lanlife.basic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户定位数据
 *
 * @author lan
 * @version 1.0
 * @date 2021/9/12 22:23
 */
@Data
@TableName("basic.bs_user_location")
public class UserLocation {

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 用户 id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 精度
     */
    @TableField("longitude")
    private double longitude;

    /**
     * 纬度
     */
    @TableField("latitude")
    private double latitude;

    /**
     * 精度
     */
    @TableField("accuracy")
    private float accuracy;

    /**
     * 提供者
     */
    @TableField("provider")
    private String provider;

    /**
     * 星数
     */
    @TableField("satellites")
    private int satellites;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
