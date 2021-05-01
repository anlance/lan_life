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
import java.util.*;

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

//    private static Logger log = LoggerFactory.getLogger(AppFilter.class);

    @Value("${common.api.excludeUrls:/basic/*}")
    private String excludeUrls;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("init AppFilter");
        if (StringUtils.isNotEmpty(excludeUrls)) {
            log.info("excludeUrls: {}", excludeUrls);
            String[] urls = excludeUrls.split(",");
            EXCLUDE_URLS.addAll(Arrays.asList(urls));
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
