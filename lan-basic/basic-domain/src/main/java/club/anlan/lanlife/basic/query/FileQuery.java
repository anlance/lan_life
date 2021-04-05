package club.anlan.lanlife.basic.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件查询条件
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/5 11:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileQuery {

    private int pageNum = 1;

    private int pageSize = 10;

    private String userId;

    private Integer deleteFlag;
}
