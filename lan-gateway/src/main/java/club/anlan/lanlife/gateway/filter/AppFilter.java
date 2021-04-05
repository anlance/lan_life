package club.anlan.lanlife.gateway.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.MimeHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * AppFilter
 *
 * @author lan
 */
@WebFilter(urlPatterns = "/*", filterName = "appFilter")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AppFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(AppFilter.class);

    private static final List<String> EXCLUDE_URLS = new ArrayList<String>();

    @Value("${common.api.excludeUrls}")
    private String excludeUrls;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.debug("init AppFilter");
        if (StringUtils.isNotEmpty(excludeUrls)) {
            logger.info("excludeUrls: {}", excludeUrls);
            String[] urls = excludeUrls.split(",");
            EXCLUDE_URLS.addAll(Arrays.asList(urls));
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        logger.info("doFilter AppFilter: {}", request.getRequestURI());
        System.out.println("doFilter AppFilter : "+ request.getRequestURI());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
