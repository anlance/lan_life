package club.anlan.lanlife.home.client;

import club.anlan.lanlife.component.base.result.ResultMessage;
import club.anlan.lanlife.home.client.fallback.factory.BasicClientFallBackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2025/2/13 11:46
 */
@FeignClient(name = "BASIC", fallbackFactory = BasicClientFallBackFactory.class)
public interface BasicClient {

    @RequestMapping(method = RequestMethod.GET, path = "/basic/user/getSalt")
    ResultMessage getSalt(@RequestParam String username);
}
