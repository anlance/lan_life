package club.anlan.lanlife.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * PostFilter
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/30 0:08
 */
@Component
@Slf4j
public class PostFilter extends ZuulFilter {


    /**
     * @Fields REQUEST_ID: 请求ID
     */
    public static final String REQUEST_ID = "Request-ID";

    @Value("${auth.post.enable:true}")
    private boolean enbalePost;

    @Override
    public boolean shouldFilter() {
        return enbalePost;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        Long beginTime = (Long) request.getAttribute("beginTime");
        if (beginTime != null) {
            long endTime = System.currentTimeMillis();
            log.info("costs: {}ms", (endTime - beginTime));
        } else {
            log.warn("beginTime is null.");
        }
        this.setRequestId(request, response);
        if (response.getStatus() == HttpServletResponse.SC_UNAUTHORIZED
                && StringUtils.isNotEmpty(request.getHeader("Unique-Code"))) {
            response.setStatus(HttpServletResponse.SC_OK);
        }
        return null;
    }

    /**
     * 向HttpServletResponse中设置Request-ID
     *
     * @date 2019年11月19日 上午10:33:43
     * @author 27477
     * @param request
     * @param response
     */
    private void setRequestId(HttpServletRequest request, HttpServletResponse response) {
        String requestId = request.getHeader(REQUEST_ID);
        if (StringUtils.isNoneEmpty(requestId)) {
            log.info("requestId: {}", requestId);
            response.setHeader(REQUEST_ID, requestId);
        }
        // response.setHeader("Strict-Transport-Security", "max-age=0");
        // response.setHeader("X-Frame-Options", "DENY");
    }

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

}

