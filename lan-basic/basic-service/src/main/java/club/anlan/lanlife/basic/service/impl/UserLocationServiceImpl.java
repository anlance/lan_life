package club.anlan.lanlife.basic.service.impl;

import club.anlan.lanlife.basic.domain.Result;
import club.anlan.lanlife.basic.domain.UserLocation;
import club.anlan.lanlife.basic.mapper.UserLocationMapper;
import club.anlan.lanlife.basic.service.UserLocationService;
import club.anlan.lanlife.component.base.helper.RestClient5sService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    @Resource
    private RestClient5sService restClient5sService;

    @Value("${map.webKey}")
    private String webKey;

    private static String URL = "https://restapi.amap.com/v3/geocode/regeo?key=%s&location=";


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
            Result result = JSONObject.parseObject(restClient5sService.get(String.format(URL, webKey) + userLocation.getLongitude() + "," + userLocation.getLatitude()), Result.class);
            log.info(JSONObject.toJSONString(result));
            userLocation.setLocationDetail(result);
        }
    }


    @Override
    public List<List<Double>> getLocationList(String startTime, String endTime) {
        return new ArrayList<>();
    }
}
