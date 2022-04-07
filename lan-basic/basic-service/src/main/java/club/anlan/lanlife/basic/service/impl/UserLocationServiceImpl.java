package club.anlan.lanlife.basic.service.impl;

import club.anlan.lanlife.basic.domain.Result;
import club.anlan.lanlife.basic.domain.UserLocation;
import club.anlan.lanlife.basic.mapper.UserLocationMapper;
import club.anlan.lanlife.basic.service.UserLocationService;
import club.anlan.lanlife.basic.util.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class UserLocationServiceImpl implements UserLocationService {

    @Autowired
    private UserLocationMapper userLocationMapper;

    private String url = "https://restapi.amap.com/v3/geocode/regeo?key=a99c13add6019c305de02c3096ff616a&location=";


    @Override
    public void saveOrUpdate(UserLocation userLocation) {
        setDetail(userLocation);
        if (StringUtils.isEmpty(userLocation.getId())) {
            userLocationMapper.insert(userLocation);
        } else {
            userLocationMapper.updateById(userLocation);
        }
    }


    private void setDetail(UserLocation userLocation) {
        if (userLocation.getLatitude() != null && userLocation.getLongitude() != null) {
            Result result = JSONObject.parseObject(HttpUtil.doSimpleGet(url + userLocation.getLongitude() + "," + userLocation.getLatitude()), Result.class);
            log.info(JSONObject.toJSONString(result));
            userLocation.setLocationDetail(result);
        }
    }
}
