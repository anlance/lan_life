//package club.anlan.lanlife.component.base.aspect;
//
//import club.anlan.lanlife.component.base.annotation.Log;
//import com.alibaba.fastjson.JSON;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.ArrayUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.*;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.core.NamedThreadLocal;
//import org.springframework.http.HttpMethod;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.lang.reflect.Method;
//import java.util.Collection;
//import java.util.Map;
//import java.util.Objects;
//
///**
// * 操作日志记录处理
// *
// * @author lan
// * @version 1.0
// * @date 2021/5/4 13:32
// */
//@Aspect
//@Component("commonLogAspect")
//@Slf4j
//public class LogAspect {
//
//    private static final int MAX_PARAM_LENGTH = 2000;
//
//    private final ThreadLocal<Long> requestTime = new NamedThreadLocal<>("request time");
//
//
//    @Pointcut("@annotation(club.anlan.lanlife.component.base.annotation.Log)")
//    public void logPointCut() {
//    }
//
//    @Before("logPointCut()")
//    public void doBefore(final JoinPoint joinPoint) {
//        Log controllerLog = getAnnotationLog(joinPoint);
//        if (controllerLog == null) {
//            return;
//        }
//        this.requestTime.set(System.currentTimeMillis());
//        log.info("【{}】 接口进入==========================================================> ", controllerLog.description());
//        if (log.isDebugEnabled()) {
//            log.debug("【{}】,请求参数:{}", controllerLog.description(), getRequestParam(joinPoint));
//        }
//    }
//
//    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
//    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
//        handleLog(joinPoint, null, jsonResult);
//        requestTime.remove();
//    }
//
//    @AfterThrowing(value = "logPointCut()", throwing = "e")
//    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
//        handleLog(joinPoint, e, null);
//        requestTime.remove();
//    }
//
//    private void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) {
//        try {
//            Log controllerLog = getAnnotationLog(joinPoint);
//            if (controllerLog == null) {
//                return;
//            }
//            log.info("【{}】 接口返回<========================================================== ，耗时【{}】ms", controllerLog.description(), System.currentTimeMillis() - requestTime.get());
//            if (log.isDebugEnabled()) {
//                log.debug("【{}】,返回结果:{}", controllerLog.description(), JSON.toJSONString(jsonResult));
//            }
//            if (controllerLog.saveToDb()) {
//
//            }
//        } catch (Exception exp) {
//            log.error("切面处理日志时失败", exp);
//        }
//    }
//
//
//    private Log getAnnotationLog(JoinPoint joinPoint) {
//        Signature signature = joinPoint.getSignature();
//        MethodSignature methodSignature = (MethodSignature) signature;
//        Method method = methodSignature.getMethod();
//        if (method != null) {
//            return method.getAnnotation(Log.class);
//        }
//        return null;
//    }
//
//    private String getRequestParam(JoinPoint joinPoint) {
//        try {
//            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            String requestMethod = requestAttributes.getRequest().getMethod();
//            if (Objects.equals(HttpMethod.PUT.name(), requestMethod) || Objects.equals(HttpMethod.POST.name(), requestMethod)) {
//                return StringUtils.substring(argsArrayToString(joinPoint.getArgs()), 0, MAX_PARAM_LENGTH);
//            }
//            final Map<String, String[]> parameterMap = requestAttributes.getRequest().getParameterMap();
//            return StringUtils.substring(JSON.toJSONString(parameterMap), 0, MAX_PARAM_LENGTH);
//        } catch (Exception e) {
//            log.error("请求参数获取失败", e);
//            return "请求参数获取失败";
//        }
//    }
//
//    private String argsArrayToString(Object[] paramsArray) {
//        StringBuilder params = new StringBuilder();
//        if (ArrayUtils.isNotEmpty(paramsArray)) {
//            for (Object o : paramsArray) {
//                if (needResolve(o)) {
//                    params.append(JSON.toJSONString(o)).append(" ");
//                }
//            }
//        }
//        return params.toString().trim();
//    }
//
//    @SuppressWarnings("rawtypes")
//    public boolean needResolve(final Object o) {
//        if (o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse) {
//            return false;
//        }
//        Class<?> clazz = o.getClass();
//        if (clazz.isArray()) {
//            return !clazz.getComponentType().isAssignableFrom(MultipartFile.class);
//        }
//        if (Collection.class.isAssignableFrom(clazz)) {
//            Collection collection = (Collection) o;
//            for (Object value : collection) {
//                if (value instanceof MultipartFile) {
//                    return false;
//                }
//            }
//            return true;
//        }
//        if (Map.class.isAssignableFrom(clazz)) {
//            Map map = (Map) o;
//            for (Object value : map.entrySet()) {
//                Map.Entry entry = (Map.Entry) value;
//                if (entry.getValue() instanceof MultipartFile) {
//                    return false;
//                }
//            }
//            return true;
//        }
//        return true;
//    }
//}
