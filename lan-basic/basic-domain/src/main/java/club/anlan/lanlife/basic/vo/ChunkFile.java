package club.anlan.lanlife.basic.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 大文件
 *
 * @author lan
 * @version 1.0
 * @date 2021/5/15 10:50
 */
@Data
public class ChunkFile {

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件 md5 值
     */
    private String fileMd5;

    /**
     * 总分片数
     */
    private Integer allIndex;

    /**
     * 当前分片数
     */
    private Integer curIndex;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 当前分片大小
     */
    private Long chunkSize;

    /**
     * 文件
     */
    List<MultipartFile> files;
}
