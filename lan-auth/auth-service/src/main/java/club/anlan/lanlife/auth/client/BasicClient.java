package club.anlan.lanlife.auth.client;

import club.anlan.lanlife.auth.client.fallback.BasicClientFallbackFactory;
import club.anlan.lanlife.auth.dto.UserDto;
import club.anlan.lanlife.component.base.result.ResultMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 基础调用客户端
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/3 23:12
 */
@FeignClient(name = "basic", path = "/basic", fallbackFactory = BasicClientFallbackFactory.class)
public interface BasicClient {

    /**
     * 获取用户信息
     */
    @RequestMapping(value = "/user/inner/getUser", method = RequestMethod.POST)
    ResultMessage getUser(@RequestBody UserDto.Query query);
}
