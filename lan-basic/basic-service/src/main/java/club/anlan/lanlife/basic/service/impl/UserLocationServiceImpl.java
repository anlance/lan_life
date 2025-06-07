package club.anlan.lanlife.basic.service.impl;

import club.anlan.lanlife.basic.domain.Result;
import club.anlan.lanlife.basic.domain.UserLocation;
import club.anlan.lanlife.basic.mapper.UserLocationMapper;
import club.anlan.lanlife.basic.service.UserLocationService;
import club.anlan.lanlife.basic.util.HttpUtil;
import club.anlan.lanlife.component.base.util.DateUtil;
//import club.anlan.lanlife.component.redis.cache.UserSessionInfo;
//import club.anlan.lanlife.component.redis.util.UserSessionUtil;
import club.anlan.lanlife.component.utils.StringUtil;
import club.anlan.lanlife.component.utils.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
//import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public List<List<Double>> getLocationList(String startTime, String endTime) {
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
//        UserSessionInfo userSessionInfo = UserSessionUtil.getUserSessionInfo();
//        if (Objects.nonNull(userSessionInfo)) {
//            userId = userSessionInfo.getUserId();
//        }
//        List<UserLocation> ulList = new LambdaQueryChainWrapper<>(userLocationMapper)
//                .eq(UserLocation::getUserId, userId)
//                .ge(UserLocation::getCreateTime, start)
//                .le(UserLocation::getCreateTime, end)
//                .list();
        List<UserLocation> ulList = new ArrayList<>();
        List<List<Double>> res = new ArrayList<>();
        double x = 0.0;
        double y = 0.0;
        if (CollectionUtil.isNotEmpty(ulList)) {
            x = ulList.get(0).getLongitude();
            y = ulList.get(0).getLatitude();
            List<Double> bd = new ArrayList<>(2);
            bd.add(x);
            bd.add(y);
            res.add(bd);
        }
        for (int i = 1; i < ulList.size(); i++) {
            UserLocation ul = ulList.get(i);
            // 不超过200M,去掉下一个点
            if (calculateDistance(x, y, ul.getLongitude(), ul.getLatitude()) > 200) {
                List<Double> bd = new ArrayList<>(2);
                bd.add(ul.getLongitude());
                bd.add(ul.getLatitude());
                res.add(bd);
                x = ul.getLongitude();
                y = ul.getLatitude();
            }
        }
        return res;
    }


    private static int calculateDistance(double x1, double y1, double x2, double y2) {
        final double NF_PI = 0.01745329251994329;
        x1 *= NF_PI;
        y1 *= NF_PI;
        x2 *= NF_PI;
        y2 *= NF_PI;
        double sinx1 = Math.sin(x1);
        double siny1 = Math.sin(y1);
        double cosx1 = Math.cos(x1);
        double cosy1 = Math.cos(y1);
        double sinx2 = Math.sin(x2);
        double siny2 = Math.sin(y2);
        double cosx2 = Math.cos(x2);
        double cosy2 = Math.cos(y2);
        double[] v1 = new double[3];
        v1[0] = cosy1 * cosx1 - cosy2 * cosx2;
        v1[1] = cosy1 * sinx1 - cosy2 * sinx2;
        v1[2] = siny1 - siny2;
        double dist = Math.sqrt(v1[0] * v1[0] + v1[1] * v1[1] + v1[2] * v1[2]);
        return (int) (Math.asin(dist / 2) * 12742001.5798544);
    }
}
