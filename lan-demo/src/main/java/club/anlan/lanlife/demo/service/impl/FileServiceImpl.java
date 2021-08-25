package club.anlan.lanlife.demo.service.impl;

import club.anlan.lanlife.demo.domain.File;
import club.anlan.lanlife.demo.mapper.FileMapper;
import club.anlan.lanlife.demo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文件 serviceImpl
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/4 13:55
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileMapper fileMapper;


    @Override
    public int insertFile(File file) {
        return fileMapper.insert(file);
    }

    @Override
    public int deleteFile(String id) {
        return fileMapper.deleteById(id);
    }

    @Override
    public int updateFile(File file) {
        return fileMapper.updateById(file);
    }

    @Override
    public File getFile(String id) {
        return fileMapper.selectById(id);
    }

}
