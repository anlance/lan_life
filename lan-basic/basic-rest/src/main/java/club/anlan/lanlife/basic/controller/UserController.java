package club.anlan.lanlife.basic.controller;

import club.anlan.lanlife.basic.dto.UserDto;
import club.anlan.lanlife.basic.service.UserService;
import club.anlan.lanlife.basic.vo.UserVo;
import club.anlan.lanlife.component.base.annotation.Log;
import club.anlan.lanlife.component.base.result.ResultMessage;
import club.anlan.lanlife.basic.domain.UserLocation;
import club.anlan.lanlife.basic.service.UserLocationService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private UserService userService;

    @Value("${map.web.key:d4de89662ced5ecb077a5ed25ec997b0}")
    private String mapWebKey = "d4de89662ced5ecb077a5ed25ec997b0";

    @Log(description = "保存定位信息")
    @PostMapping("/saveLocation")
    public ResultMessage saveLocation(@RequestBody UserLocation userLocation) throws IOException {
        userLocationService.saveOrUpdate(userLocation);
        return ResultMessage.createSuccessResult();
    }


    @Log(description = "内部服务获取用户信息")
    @PostMapping("/inner/getUser")
    public ResultMessage getUser(@RequestBody UserDto.Query query) {
        return ResultMessage.createSuccessResult(userService.getUser(query));
    }


    @Log(description = "添加用户")
    @PostMapping("/create")
    public ResultMessage createUser(@RequestBody UserVo.AddVo addUserVo) {
        userService.saveOrUpdate(addUserVo.toUser());
        return ResultMessage.createSuccessResult();
    }

    @Log(description = "获取盐和随机数")
    @GetMapping("/getSalt")
    public ResultMessage getSalt(@RequestParam String username) {
        return ResultMessage.createSuccessResult(userService.getSalt(username));
    }

    @Log(description = "获取地图webKey")
    @GetMapping("/webMap/getMapWebKey")
    public ResultMessage getMapWebKey() {
        return ResultMessage.createSuccessResult(mapWebKey);
    }

    @Log(description = "获取坐标数组")
    @GetMapping("/webMap/getLocationList")
    public ResultMessage getLocationList(@RequestParam(value = "startTime", required = false)String startTime,
                                      @RequestParam(value = "endTime",required = false) String endTime) {
        return ResultMessage.createSuccessResult(userLocationService.getLocationList(startTime, endTime));
    }
}
