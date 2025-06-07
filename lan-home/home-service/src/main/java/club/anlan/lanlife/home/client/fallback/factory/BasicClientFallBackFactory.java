package club.anlan.lanlife.home.client.fallback.factory;

import club.anlan.lanlife.home.client.BasicClient;
import club.anlan.lanlife.home.client.fallback.BasicClientFallBack;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2025/2/13 11:47
 */
@Component
@Slf4j
public class BasicClientFallBackFactory implements FallbackFactory<BasicClient> {
    private final BasicClientFallBack basicClientFallBack;

    public BasicClientFallBackFactory(BasicClientFallBack basicClientFallBack) {
        this.basicClientFallBack = basicClientFallBack;
    }

    @Override
    public BasicClient create(Throwable throwable) {
        log.error("请求basic服务失败", throwable);
        return basicClientFallBack;
    }
}
