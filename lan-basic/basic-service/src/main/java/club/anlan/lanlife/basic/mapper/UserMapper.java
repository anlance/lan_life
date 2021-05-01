package club.anlan.lanlife.basic.mapper;

import club.anlan.lanlife.basic.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2021/5/1 11:05
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
