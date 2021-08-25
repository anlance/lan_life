package club.anlan.lanlife.demo.mapper;

import club.anlan.lanlife.demo.domain.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件 mapper
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/4 13:56
 */
@Mapper
public interface FileMapper extends BaseMapper<File> {

}
