package club.anlan.lanlife.gateway.config;


import org.springframework.cloud.netflix.ribbon.apache.RibbonApacheHttpRequest;
import org.springframework.cloud.netflix.ribbon.apache.RibbonLoadBalancingHttpClient;
import org.springframework.cloud.netflix.ribbon.support.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.apache.HttpClientRibbonCommand;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/30 0:45
 */
public class CustomHttpClientRibbonCommand extends HttpClientRibbonCommand {

    public CustomHttpClientRibbonCommand(String commandKey, RibbonLoadBalancingHttpClient client, RibbonCommandContext context,
                                         ZuulProperties zuulProperties) {
        super(commandKey, client, context, zuulProperties);
    }

    public CustomHttpClientRibbonCommand(String commandKey, RibbonLoadBalancingHttpClient client, RibbonCommandContext context,
                                         ZuulProperties zuulProperties, FallbackProvider zuulFallbackProvider) {
        super(commandKey, client, context, zuulProperties, zuulFallbackProvider);
    }

    @Override
    protected RibbonApacheHttpRequest createRequest() throws Exception {
        RibbonApacheHttpRequest ribbonApacheHttpRequest = new CustomRibbonApacheHttpRequest(this.context);
        return ribbonApacheHttpRequest;
    }

}
