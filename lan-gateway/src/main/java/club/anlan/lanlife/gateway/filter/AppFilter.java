package club.anlan.lanlife.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
