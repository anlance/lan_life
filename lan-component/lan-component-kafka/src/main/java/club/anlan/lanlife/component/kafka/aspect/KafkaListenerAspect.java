package club.anlan.lanlife.component.kafka.aspect;

import club.anlan.lanlife.component.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2023/8/14 0:00
 */
@Aspect
@Component("kafkaListenerAspect")
@Slf4j
public class KafkaListenerAspect {

    @Pointcut("@annotation(org.springframework.kafka.annotation.KafkaListener)")
    public void logPointCut() {
    }

    @Before("logPointCut()")
    public void doBefore(final JoinPoint joinPoint) {
        setTraceId(joinPoint);
    }

    @AfterReturning(pointcut = "logPointCut()", returning = "obj")
    public void doAfterReturning(JoinPoint joinPoint, Object obj) {
        removeTraceId(joinPoint);
    }

    @AfterThrowing(value = "logPointCut()", throwing = "ex")
    public void doAfterThrowing(JoinPoint joinPoint, Exception ex) {
        removeTraceId(joinPoint);
    }

    private void setTraceId(JoinPoint joinPoint) {
        try {
            String traceId = getTraceId(joinPoint);
            if (traceId != null) {
                MDC.put("traceId", traceId);
            }
        } catch (Exception e) {
            log.error("set traceId failed, ", e);
        }
    }

    private void removeTraceId(JoinPoint joinPoint) {
        try {
            MDC.remove("traceId");
        } catch (Exception e) {
            log.error("remove traceId failed, ", e);
        }
    }

    private String getTraceId(JoinPoint joinPoint) {
        String traceId = getTraceId(joinPoint.getArgs());
        if (StringUtil.isNotEmpty(traceId)) {
            return traceId;
        }
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (int i = 0; i < parameterAnnotations.length; i++) {
                Annotation[] annotations = parameterAnnotations[i];
                for (int j = 0; j < annotations.length; j++) {
                    Annotation annotation = annotations[j];
                    if (annotation instanceof org.springframework.messaging.handler.annotation.Header &&
                            "traceId".equals(((org.springframework.messaging.handler.annotation.Header) annotation).value())) {
                        byte[] arg = (byte[]) joinPoint.getArgs()[i];
                        return new String(arg);
                    } else if (annotation instanceof org.springframework.messaging.handler.annotation.Headers) {
                        return new String((byte[]) ((Map) joinPoint.getArgs()[i]).get("traceId"));
                    }
                }
            }
        }
        return null;
    }

    private String getTraceId(Object[] paramsArray) {
        if (ArrayUtils.isNotEmpty(paramsArray)) {
            for (Object o : paramsArray) {
                if (o instanceof ConsumerRecord) {
                    return new String(((ConsumerRecord<?, ?>) o).headers().lastHeader("traceId").value());
                }
            }
        }
        return null;
    }
}
