package club.anlan.lanlife.basic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/24 15:28
 */
@Data
@TableName("basic.bs_user")
public class User {

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 用户名
     */
    @TableField("login_name")
    private String loginName;

    /**
     * 密码
     */
    @TableField("login_pass")
    private String loginPass;

    /**
     * 密码加密盐值
     */
    private String salt;

    /**
     * 昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 用户邮箱
     */
    @TableField("user_email")
    private String userEmail;

    /**
     * 用户头像地址
     */
    @TableField("user_pic")
    private String userPic;

    /**
     * 描述
     */
    private String memo;


    /**
     * 最后一次登录时间
     */
    @TableField("last_login_date")
    private LocalDateTime lastLoginDate;


    /**
     * 最后登录IP
     */
    @TableField("last_login_ip")
    private String lastLoginIp;

    /**
     * 是否删除，1-被删除
     */
    @TableField("delete_flag")
    private Integer deleteFlag;


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
