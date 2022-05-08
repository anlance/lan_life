package club.anlan.lanlife.basic.service.impl;

import club.anlan.lanlife.basic.domain.Result;
import club.anlan.lanlife.basic.domain.UserLocation;
import club.anlan.lanlife.basic.mapper.UserLocationMapper;
import club.anlan.lanlife.basic.service.UserLocationService;
import club.anlan.lanlife.basic.util.HttpUtil;
import club.anlan.lanlife.component.base.util.DateUtil;
import club.anlan.lanlife.component.redis.cache.UserSessionInfo;
import club.anlan.lanlife.component.redis.util.UserSessionUtil;
import club.anlan.lanlife.component.utils.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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


    @Override
    public List<List<String>> getLocationList(String startTime, String endTime) {
        Date start = null;
        Date end = null;
        if (StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime)) {
            Date now = new Date();
            start = DateUtil.getDayBegin(now);
            end = DateUtil.getDayEnd(now);
        } else {
            start = DateUtil.stringToDate(startTime, DateUtil.DATA_FORMAT_yyyy_MM_dd_HH_mm_ss);
            end = DateUtil.stringToDate(endTime, DateUtil.DATA_FORMAT_yyyy_MM_dd_HH_mm_ss);
        }
        String userId = null;
        UserSessionInfo userSessionInfo = UserSessionUtil.getUserSessionInfo();
        if (Objects.nonNull(userSessionInfo)) {
            userId = userSessionInfo.getUserId();
        }
        List<UserLocation> ulList = new LambdaQueryChainWrapper<>(userLocationMapper)
                .eq(UserLocation::getUserId, userId)
                .ge(UserLocation::getCreateTime, start)
                .le(UserLocation::getCreateTime, end)
                .list();
        List<List<String>> res = new ArrayList<>();
        for (UserLocation ul : ulList) {
            List<String> str = new ArrayList<>(1);
            str.add(ul.getLongitude() + "," + ul.getLatitude());
            res.add(str);
        }
        return res;
    }
}
