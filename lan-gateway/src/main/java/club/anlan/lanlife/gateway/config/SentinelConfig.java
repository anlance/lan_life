package club.anlan.lanlife.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayParamFlowItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.zuul.filters.SentinelZuulErrorFilter;
import com.alibaba.csp.sentinel.adapter.gateway.zuul.filters.SentinelZuulPostFilter;
import com.alibaba.csp.sentinel.adapter.gateway.zuul.filters.SentinelZuulPreFilter;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.netflix.zuul.ZuulFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;


@Configuration
public class SentinelConfig {
    private static final String ONCE_PER_SEC_GROUP = "oncePerSec";


    @PostConstruct
    public void doInit() {
        initCustomizedApis();
        initGatewayRules();
    }

    /**
     * 定义分组限流规则，可使用gateway_rule.yml中定义的ruleId，则代表整个rule都使用该限流规则
     * 也可以自定义分组
     * 网关限流规则
     */
    private void initGatewayRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();
        rules.add(new GatewayFlowRule(ONCE_PER_SEC_GROUP)
                .setCount(5)
                .setIntervalSec(1).setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
                // 根据ip拦截
                .setParamItem(new GatewayParamFlowItem()
                        .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP))
                .setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER)
                .setMaxQueueingTimeoutMs(5000)
        );
        // 加载网关限流规则
        GatewayRuleManager.loadRules(rules);
    }

    /**
     * 自定义限流api
     */
    private void initCustomizedApis() {
        Set<ApiDefinition> definitions = new HashSet<>();
        ApiDefinition api1 = new ApiDefinition(ONCE_PER_SEC_GROUP)
                .setPredicateItems(new HashSet<ApiPredicateItem>() {
                    {
                        // 正则匹配
                        add(new ApiPathPredicateItem().setPattern("/basic/auth/.*?/checkUserAccountByPhone")
                                .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_REGEX))
                        ;
                        add(new ApiPathPredicateItem().setPattern("/home/system/**")
                                .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX))
                        ;
                    }
                });
        definitions.add(api1);
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }

    @Bean
    public ZuulFilter sentinelZuulPreFilter() {
        // We can also provider the filter order in the constructor.
        return new SentinelZuulPreFilter();
    }

    @Bean
    public ZuulFilter sentinelZuulPostFilter() {
        return new SentinelZuulPostFilter();
    }

    @Bean
    public ZuulFilter sentinelZuulErrorFilter() {
        return new SentinelZuulErrorFilter();
    }

}
