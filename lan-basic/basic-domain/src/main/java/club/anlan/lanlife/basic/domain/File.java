package club.anlan.lanlife.basic.domain;

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
     * 是否删除，0-被删除
     */
    private Integer deleteFlag;

    /**
     * 创建时间
     */
    private Date createTime;
}
