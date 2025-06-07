//package club.anlan.lanlife.component.base.filter;
//
//import club.anlan.lanlife.component.utils.StringUtil;
//import org.slf4j.MDC;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.util.UUID;
//
///**
// * 公共链路追踪filter，需要配合log4j2.xml中使用
// *
// * @author lan
// * @version 1.0
// * @date 2022/3/29 0:40
// */
////@Component("commonMdcFilter")
//@WebFilter(urlPatterns = "/*", filterName = "commonMdcFilter")
//public class MdcFilter implements Filter {
//
//    public static final String TRACE_ID = "traceId";
//
//    @Autowired
//    private Environment environment;
//
//    @Override
//    public void init(FilterConfig filterConfig) {
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        try {
//            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//            if (StringUtil.isNotBlank(httpServletRequest.getHeader(TRACE_ID))) {
//                MDC.put(TRACE_ID, httpServletRequest.getHeader(TRACE_ID));
//            } else {
//                // 在高并发情况下可以考虑别的生成UUID的方式
//                MDC.put(TRACE_ID, getRemoteHost(request) + UUID.randomUUID().toString().replace("-", ""));
//            }
//            chain.doFilter(request, response);
//        } catch (Exception e) {
//            chain.doFilter(request, response);
//        } finally {
//            MDC.remove(TRACE_ID);
//        }
//    }
//
//    @Override
//    public void destroy() {
//    }
//
//    private String getRemoteHost(ServletRequest request) {
//        try {
//            return environment.getProperty("spring.application.name") + "-" + request.getRemoteHost() + "-";
//        } catch (Exception e) {
//            return "unknown";
//        }
//    }
//}
