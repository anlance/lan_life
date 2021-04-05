package club.anlan.lanlife.basic.service.impl;

import club.anlan.lanlife.basic.domain.File;
import club.anlan.lanlife.basic.enums.DeleteFlagEnum;
import club.anlan.lanlife.basic.mapper.FileMapper;
import club.anlan.lanlife.basic.query.FileQuery;
import club.anlan.lanlife.basic.service.FileService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类
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

    @Override
    public IPage<File> getFiles(FileQuery fileQuery) {
        return new LambdaQueryChainWrapper<>(fileMapper)
                .eq(File::getDeleteFlag, fileQuery.getDeleteFlag())
                .eq(File::getCreateUserId, fileQuery.getUserId())
                .orderByDesc(File::getCreateTime)
                .page(new Page<>(fileQuery.getPageNum(), fileQuery.getPageSize()));
    }


}
