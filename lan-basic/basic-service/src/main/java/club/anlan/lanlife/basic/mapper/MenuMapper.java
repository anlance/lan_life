package club.anlan.lanlife.basic.mapper;

import club.anlan.lanlife.basic.domain.Menu;
import club.anlan.lanlife.basic.vo.MenuVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单
 *
 * @author lan
 * @version 1.0
 * @date 2022/5/4 9:41
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 获取菜单列表
     *
     * @param userId 用户ID
     */
    List<MenuVo> listUserMenu(@Param("userId") String userId);
}
