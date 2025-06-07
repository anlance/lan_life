package club.anlan.lanlife.home.client.fallback;

import club.anlan.lanlife.component.base.result.ResultMessage;
import club.anlan.lanlife.home.client.BasicClient;
import org.springframework.stereotype.Component;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2025/2/13 11:48
 */
@Component
public class BasicClientFallBack implements BasicClient {

    @Override
    public ResultMessage getSalt(String username) {
        return ResultMessage.createFailedResult("error getSalt {}", username);
    }
}
