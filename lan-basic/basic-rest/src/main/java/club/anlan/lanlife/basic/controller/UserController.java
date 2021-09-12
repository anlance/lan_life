package club.anlan.lanlife.basic.controller;

import club.anlan.lanlife.base.annotation.Log;
import club.anlan.lanlife.base.result.ResultMessage;
import club.anlan.lanlife.basic.domain.UserLocation;
import club.anlan.lanlife.basic.service.UserLocationService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 用户 控制器
 *
 * @author lan
 * @version 1.0
 * @date 2021/9/12 22:25
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserLocationService userLocationService;

    @Log(description = "保存定位信息")
    @PostMapping("/saveLocation")
    public ResultMessage saveLocation(@RequestBody UserLocation userLocation) throws IOException {
        log.info("te {}", JSONObject.toJSONString(userLocation));
        userLocationService.saveOrUpdate(userLocation);
        return ResultMessage.createSuccessResult();
    }

}
