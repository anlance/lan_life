package club.anlan.lanlife.auth.config;

import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义异常处理类
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/4 18:21
 */
public class MyExceptionHandlerExceptionResolver extends ExceptionHandlerExceptionResolver {

    private Map<ControllerAdviceBean, ExceptionHandlerMethodResolver> exceptionHandlerAdviceCache = null;

    @Override
    protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception) {
        List<ControllerAdviceBean> adviceBeans = ControllerAdviceBean.findAnnotatedBeans(getApplicationContext());
        if (exceptionHandlerAdviceCache==null){
            exceptionHandlerAdviceCache = new LinkedHashMap<>();
            for (ControllerAdviceBean adviceBean:adviceBeans){
                ExceptionHandlerMethodResolver resolver = new ExceptionHandlerMethodResolver(adviceBean.getBeanType());
                exceptionHandlerAdviceCache.put(adviceBean, resolver);
            }
        }
        Class<?> handlerType = (handlerMethod != null ? handlerMethod.getBeanType() : null);
        for (Map.Entry<ControllerAdviceBean, ExceptionHandlerMethodResolver> entry : this.exceptionHandlerAdviceCache.entrySet()) {
            if (entry.getKey().isApplicableToBeanType(handlerType)) {
                ExceptionHandlerMethodResolver resolver = entry.getValue();
                Method method = resolver.resolveMethod(exception);
                if (method != null) {
                    return new ServletInvocableHandlerMethod(entry.getKey().resolveBean(), method);
                }
            }
        }
        return null;
    }
}

