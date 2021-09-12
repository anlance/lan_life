package club.anlan.lanlife.basic.service;

import club.anlan.lanlife.basic.domain.UserLocation;
import org.springframework.stereotype.Service;

/**
 * 用户定位信息 service
 *
 * @author lan
 * @version 1.0
 * @date 2021/9/12 22:43
 */
public interface UserLocationService {

    /**
     * 保存或者更新用户定位信息
     * @param userLocation 用户定位信息
     */
    void saveOrUpdate(UserLocation userLocation);
}
