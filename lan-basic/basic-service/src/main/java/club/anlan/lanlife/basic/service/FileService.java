package club.anlan.lanlife.basic.service;

import club.anlan.lanlife.basic.domain.File;
import club.anlan.lanlife.basic.query.FileQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/4 13:54
 */
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


    /**
     * 查询文件
     */
    IPage<File> getFiles(FileQuery fileQuery);
}
