package club.anlan.lanlife.gateway.filter;

import club.anlan.lanlife.component.utils.constant.Constants;
import club.anlan.lanlife.gateway.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.MimeHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * AppFilter
 *
 * @author lan
 */
@WebFilter(urlPatterns = "/*", filterName = "appFilter")
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class AppFilter implements Filter {

    private static final List<String> EXCLUDE_URLS = new ArrayList<String>();

    private static final String AUTH_CURRENT = "/auth/user/current";

    @Value("${common.api.excludeUrls:/basic/*}")
    private String excludeUrls;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("init AppFilter");
        if (StringUtils.isNotEmpty(excludeUrls)) {
            log.info("excludeUrls: {}", excludeUrls);
            String[] urls = excludeUrls.split(",");
            EXCLUDE_URLS.addAll(Arrays.asList(urls));
            EXCLUDE_URLS.remove(AUTH_CURRENT);
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info("doFilter AppFilter: {}", request.getRequestURI());
        String requestUri = request.getRequestURI();
        for (String excludeUrl : EXCLUDE_URLS) {
            if (PatternMatchUtils.simpleMatch(excludeUrl, requestUri)) {
                this.removeHeader(request, Constants.AUTHORIZATION);
                break;
            }
        }
        String ticket = request.getParameter(Constant.TICKET);
        if (StringUtils.isNotEmpty(ticket)) {
            log.info("ticket: {}", ticket);
            this.addHeader(request, Constants.AUTHORIZATION, Constants.BEARER_EXT + ticket);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

    private void removeHeader(HttpServletRequest request, String key) {
        Class<? extends HttpServletRequest> requestClass = request.getClass();
        try {
            Field requestField = requestClass.getDeclaredField("request");
            requestField.setAccessible(true);
            Object requestObject = requestField.get(request);
            Field coyoteRequestField = requestObject.getClass().getDeclaredField("coyoteRequest");
            coyoteRequestField.setAccessible(true);
            Object requestObject2 = coyoteRequestField.get(requestObject);
            Field headersField = requestObject2.getClass().getDeclaredField("headers");
            headersField.setAccessible(true);
            MimeHeaders mimeHeaders = (MimeHeaders) headersField.get(requestObject2);
            mimeHeaders.removeHeader(key);
        } catch (Exception e) {
            log.error("移除header失败", e);
        }
    }


    private void addHeader(HttpServletRequest request, String key, String value) {
        Class<? extends HttpServletRequest> requestClass = request.getClass();
        try {
            Field requestField = requestClass.getDeclaredField("request");
            requestField.setAccessible(true);
            Object requestObject = requestField.get(request);
            Field coyoteRequestField = requestObject.getClass().getDeclaredField("coyoteRequest");
            coyoteRequestField.setAccessible(true);
            Object requestObject2 = coyoteRequestField.get(requestObject);
            Field headersField = requestObject2.getClass().getDeclaredField("headers");
            headersField.setAccessible(true);
            MimeHeaders mimeHeaders = (MimeHeaders) headersField.get(requestObject2);
            mimeHeaders.addValue(key).setString(value);
        } catch (Exception e) {
            log.error("添加header失败", e);
        }
    }
}
