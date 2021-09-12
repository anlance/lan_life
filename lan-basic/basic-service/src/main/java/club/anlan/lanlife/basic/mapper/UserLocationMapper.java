package club.anlan.lanlife.basic.mapper;

import club.anlan.lanlife.basic.domain.UserLocation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户定位 mapper
 *
 * @author lan
 * @version 1.0
 * @date 2021/9/12 22:42
 */
@Mapper
public interface UserLocationMapper extends BaseMapper<UserLocation> {
}
