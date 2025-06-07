package club.anlan.lanlife.basic.service.impl;

import club.anlan.lanlife.basic.mapper.MenuMapper;
import club.anlan.lanlife.basic.service.MenuService;
import club.anlan.lanlife.basic.vo.MenuVo;
import club.anlan.lanlife.component.base.constant.Constants;
//import club.anlan.lanlife.component.redis.cache.UserSessionInfo;
//import club.anlan.lanlife.component.redis.util.UserSessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 菜单
 *
 * @author lan
 * @version 1.0
 * @date 2022/5/4 9:53
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<MenuVo> listUserMenu() {
        String userId = Constants.DEFAULT_USER_ID;
//        UserSessionInfo userSessionInfo = UserSessionUtil.getUserSessionInfo();
//        if(Objects.nonNull(userSessionInfo)) {
//            userId = userSessionInfo.getUserId();
//        }
        return menuMapper.listUserMenu(userId);
    }

}
