package club.anlan.lanlife.gateway.config;

import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.ribbon.apache.RibbonLoadBalancingHttpClient;
import org.springframework.cloud.netflix.ribbon.support.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.cloud.netflix.zuul.filters.route.apache.HttpClientRibbonCommand;
import org.springframework.cloud.netflix.zuul.filters.route.apache.HttpClientRibbonCommandFactory;

import java.util.Set;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/30 0:45
 */
public class CustomHttpClientRibbonCommandFactory extends HttpClientRibbonCommandFactory{

    private final SpringClientFactory clientFactory;

    private final ZuulProperties zuulProperties;

    public CustomHttpClientRibbonCommandFactory(SpringClientFactory clientFactory, ZuulProperties zuulProperties) {
        super(clientFactory, zuulProperties);
        this.clientFactory = clientFactory;
        this.zuulProperties = zuulProperties;
    }

    public CustomHttpClientRibbonCommandFactory(SpringClientFactory clientFactory, ZuulProperties zuulProperties,
                                                Set<FallbackProvider> fallbackProviders) {
        super(clientFactory, zuulProperties, fallbackProviders);
        this.clientFactory = clientFactory;
        this.zuulProperties = zuulProperties;
    }

    @Override
    public HttpClientRibbonCommand create(RibbonCommandContext context) {
        FallbackProvider zuulFallbackProvider = getFallbackProvider(context.getServiceId());
        final String serviceId = context.getServiceId();
        final RibbonLoadBalancingHttpClient client = this.clientFactory.getClient(
                serviceId, RibbonLoadBalancingHttpClient.class);
        client.setLoadBalancer(this.clientFactory.getLoadBalancer(serviceId));

        return new CustomHttpClientRibbonCommand(serviceId, client, context, zuulProperties, zuulFallbackProvider);
    }
}

