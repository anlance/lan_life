package club.anlan.lanlife.gateway.filter;

import club.anlan.lanlife.component.base.filter.MdcFilter;
import club.anlan.lanlife.component.base.result.ResultMessage;
import club.anlan.lanlife.component.utils.StringUtil;
import club.anlan.lanlife.gateway.constant.Constant;
import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * InterfaceAuthFilter
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/5 18:10
 */
@Slf4j
@Component
public class InterfaceAuthFilter extends ZuulFilter {

    @Value("${common.api.excludeUrls}")
    private String excludeUrls;

    private static final List<String> EXCLUDE_URLS = new ArrayList<String>();

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx == null) {
            log.info("获取RequestContext.getCurrentContext() 为null");
            return false;
        }
        HttpServletRequest request = ctx.getRequest();
        String requestUri = request.getRequestURI();

        String requestMethod = request.getMethod();
        final String traceId = MDC.get(MdcFilter.TRACE_ID);
        ctx.addZuulRequestHeader(MdcFilter.TRACE_ID, traceId);
        log.info("requestUri: {}, requestMethod: {}, traceId: {}", requestUri, requestMethod, traceId);
        request.setAttribute("beginTime", System.currentTimeMillis());
        ResultMessage resultMessage = null;

        // 判断是否是内部接口，内部接口禁止访问
        if (requestUri.contains(Constant.INNER_INVOKE_FLAG)) {
            resultMessage = ResultMessage.createFailedResult("401", "无访问权限");
        }

        // 不鉴权
        for (String excludeUrl : EXCLUDE_URLS) {
            if (PatternMatchUtils.simpleMatch(excludeUrl, requestUri)) {
                resultMessage = null;
            }
        }

        // todo 检查权限是否变更

        // todo 校验uniqueCode

        if (resultMessage != null) {
            this.writeResponseInfo(ctx, resultMessage);
            return false;
        }

        return true;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }


    @PostConstruct
    public void init() {
        log.info("init InterfaceAuthFilter");
        if (StringUtil.isNotEmpty(excludeUrls)) {
            String[] urls = excludeUrls.split(",");
            EXCLUDE_URLS.addAll(Arrays.asList(urls));
        }
    }


    private void writeResponseInfo(RequestContext ctx, ResultMessage resultMessage) {
        if (resultMessage != null) {
            log.info("writeResponseInfo: {}", resultMessage);
            // 过滤该请求，不对其进行路由
            ctx.setSendZuulResponse(false);
            // 返回错误码
            ctx.setResponseStatusCode(200);
            // 返回错误内容
            ctx.setResponseBody(JSON.toJSONString(resultMessage));
            ctx.set("isSuccess", false);
            ctx.getResponse().setContentType("application/json;charset=UTF-8");
        }
    }
}
