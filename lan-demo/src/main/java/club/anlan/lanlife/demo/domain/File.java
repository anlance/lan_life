package club.anlan.lanlife.demo.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

/**
 * 文件
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/3 11:07
 */
@Data
@TableName("demo.bs_file")
public class File {

    /**
     * id
     */
    private String id;

//    /**
//     * 父节点id
//     */
//    @TableField("parent_id  ")
//    private String parentId;

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
     * 是否删除，0-被删除
     */
    @TableField("delete_flag")
    private Integer deleteFlag = 1;

    /**
     * 创建用户
     */
    @TableField("create_user_id")
    private String createUserId;

    /**
     * 创建时间
     */
    private Date createTime;


    public File() {
        this.setId(UUID.randomUUID().toString().replaceAll("-",""));
        this.setCreateUserId("defaultUser");
        this.setName("defaultName");
        this.setType("defaultType");
        this.setUrl("defaultUrl");
        this.setCreateTime(new Date());
    }

}
