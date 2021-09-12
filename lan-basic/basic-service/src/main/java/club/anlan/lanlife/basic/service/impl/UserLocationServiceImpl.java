package club.anlan.lanlife.basic.service.impl;

import club.anlan.lanlife.basic.domain.UserLocation;
import club.anlan.lanlife.basic.mapper.UserLocationMapper;
import club.anlan.lanlife.basic.service.UserLocationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户定位信息 实现类
 *
 * @author lan
 * @version 1.0
 * @date 2021/9/12 22:45
 */
@Service
public class UserLocationServiceImpl implements UserLocationService {

    @Autowired
    private UserLocationMapper userLocationMapper;


    @Override
    public void saveOrUpdate(UserLocation userLocation) {
        if (StringUtils.isEmpty(userLocation.getId())) {
            userLocationMapper.insert(userLocation);
        } else {
            userLocationMapper.updateById(userLocation);
        }
    }
}
