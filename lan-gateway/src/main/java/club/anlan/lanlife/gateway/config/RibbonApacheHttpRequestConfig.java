package club.anlan.lanlife.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Set;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/30 0:44
 */
@Configuration
public class RibbonApacheHttpRequestConfig {

    @Autowired(required = false)
    private Set<FallbackProvider> zuulFallbackProviders = Collections.emptySet();

    @Bean
    @ConditionalOnMissingBean
    public RibbonCommandFactory<?> ribbonCommandFactory(
            SpringClientFactory clientFactory, ZuulProperties zuulProperties) {
        return new CustomHttpClientRibbonCommandFactory(clientFactory, zuulProperties, zuulFallbackProviders);
    }
}
