package club.anlan.lanlife.demo.service;

import club.anlan.lanlife.demo.domain.File;
import org.springframework.stereotype.Service;

/**
 * 文件 service
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/4 13:54
 */
@Service()
public interface FileService {

    /**
     * 插入文件
     * @param file 文件
     */
    int insertFile(File file);

    /**
     * 删除文件
     * @param id 文件id
     */
    int deleteFile(String id);

    /**
     * 更新文件
     * @param file 文件
     */
    int updateFile(File file);

    /**
     * 查询文件
     * @param id 文件id
     */
    File getFile(String id);
}
