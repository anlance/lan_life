package club.anlan.lanlife.auth.client.fallback;

import club.anlan.lanlife.auth.client.BasicClient;
import club.anlan.lanlife.auth.dto.UserDto;
import club.anlan.lanlife.component.base.result.ResultMessage;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 基础调用客户端
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/3 23:14
 */
@Component
@Slf4j
public class BasicClientFallbackFactory implements FallbackFactory<BasicClient> {

    @Override
    public BasicClient create(Throwable cause) {
        return new BasicClient() {
            @Override
            public ResultMessage getUser(UserDto.Query query) {
                String errMsg = "请求用户信息失败";
                log.error(errMsg, cause);
                return ResultMessage.createFailedResult(errMsg);
            }
        };
    }
}
