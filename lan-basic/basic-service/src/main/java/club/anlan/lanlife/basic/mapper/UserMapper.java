package club.anlan.lanlife.basic.mapper;

import club.anlan.lanlife.basic.domain.User;
import club.anlan.lanlife.basic.dto.UserDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户
 *
 * @author lan
 * @version 1.0
 * @date 2021/5/1 11:05
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询用户
     *
     * @param query 查询条件
     * @return 用户信息
     */
    UserDto.User getUser(@Param("params") UserDto.Query query);
}
