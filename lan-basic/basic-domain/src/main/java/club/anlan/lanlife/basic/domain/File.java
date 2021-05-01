package club.anlan.lanlife.basic.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 文件
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/3 11:07
 */
@Data
@TableName("basic.bs_file")
public class File {

    /**
     * id
     */
    private String id;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件类型，文件后缀生成
     */
    private String type;

    /**
     * 存储地址
     */
    private String url;

    /**
     * 是否删除，1-被删除
     */
    @TableField("delete_flag")
    private Integer deleteFlag;

    /**
     * 创建用户
     */
    @TableField("create_user_id")
    private String createUserId;

    /**
     * 创建时间
     */
    private Date createTime;
}
