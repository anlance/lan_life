package club.anlan.lanlife.auth.security;

import club.anlan.lanlife.auth.constant.Constant;
import club.anlan.lanlife.component.base.exception.BusinessException;
import club.anlan.lanlife.component.base.exception.BusinessRuntimeException;
import club.anlan.lanlife.component.base.i18n.MessageResource;
import club.anlan.lanlife.component.base.result.ResultMessage;
import club.anlan.lanlife.component.utils.StringUtil;
import club.anlan.lanlife.component.utils.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/4 16:34
 */
@Slf4j
@ControllerAdvice
public class AuthExceptionHandler implements ResponseBodyAdvice<Object> {

    @Autowired
    private MessageResource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultMessage defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws Exception {
        String errorMsg = null;
        String errorCode = null;
        String i18nMsg = null;

        if (e instanceof BusinessException) {
            // 业务异常
            errorCode = ((BusinessException) e).getCode();
            errorMsg = ((BusinessException) e).getErrMsg();
        } else if (e instanceof BusinessRuntimeException) {
            // 业务异常
            errorCode = ((BusinessRuntimeException) e).getCode();
            errorMsg = ((BusinessRuntimeException) e).getErrMsg();
        } else if (e.getCause() instanceof BusinessException) {
            errorCode = ((BusinessException) e.getCause()).getCode();
            errorMsg = ((BusinessException) e.getCause()).getErrMsg();
        } else if (e.getCause() instanceof BusinessRuntimeException) {
            errorCode = ((BusinessRuntimeException) e.getCause()).getCode();
            errorMsg = ((BusinessRuntimeException) e.getCause()).getErrMsg();
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            response.sendError(HttpStatus.METHOD_NOT_ALLOWED.value());
        } else if (e instanceof InvalidGrantException) {
            // 密码错误
            if (Constant.ERROR_INVALID_GRANT.equals(((InvalidGrantException) e).getOAuth2ErrorCode())) {
                if (Constant.ERROR_BAD_CREDENTIALS.equals(e.getMessage())) {
                    i18nMsg = messageSource.getMessage(Constant.ERROR_USERNAME_OR_PASSWORD, request.getLocale());
                }
            }
        } else if (e instanceof InvalidScopeException) {
            // scope码有误
            if (Constant.ERROR_INVALID_SCOPE.endsWith(((InvalidScopeException) e).getOAuth2ErrorCode())) {
                i18nMsg = messageSource.getMessage(Constant.ERROR_LOGIN_FAILED, request.getLocale());
            }
        } else if (e instanceof UnsupportedGrantTypeException) {
            // 认证类型有误
            if (Constant.ERROR_UNSUPPORTED_GRANT_TYPE
                    .endsWith(((UnsupportedGrantTypeException) e).getOAuth2ErrorCode())) {
                i18nMsg = messageSource.getMessage(Constant.ERROR_LOGIN_FAILED, request.getLocale());
            }
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException me = ((MethodArgumentNotValidException) e);
            log.info(me.getMessage());
            List<ObjectError> list = me.getBindingResult().getAllErrors();
            if (CollectionUtil.isEmpty(list)) {
                errorMsg = messageSource.getMessage(Constant.SYSTEM_ERROR_PARAMETER_VALIDATE,
                        request.getLocale());
            } else {
                String i18nCode = list.get(list.size() - 1).getDefaultMessage();
                i18nMsg = messageSource.getMessage(i18nCode, request.getLocale());
            }
        }
        if (errorMsg == null) {
            if (StringUtil.isEmpty(i18nMsg)) {
                i18nMsg = messageSource.getMessage(Constant.SYSTEM_ERROR_DEFAULT, request.getLocale());
            }
            if (StringUtil.isNotEmpty(i18nMsg)) {
                int index = i18nMsg.indexOf(":");
                if (errorCode == null && index > 0) {
                    errorCode = i18nMsg.substring(0, index);
                }
                if (errorMsg == null) {
                    errorMsg = i18nMsg.substring(index + 1);
                }
            }
        }
        return ResultMessage.createFailedResult(errorCode, errorMsg);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    /**
     * 封装返回结果格式
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            return ResultMessage.createFailedResult(Constant.SYSTEM_ERROR_DEFAULT);
        }
        if (returnType.hasMethodAnnotation(ExceptionHandler.class)) {
            return body;
        } else if (body instanceof ResultMessage) {
            return body;
        } else if (body instanceof DefaultOAuth2AccessToken) {
            // ①由于改造oauth2的统一返回数据格式，导致微服务鉴权时，获取不到token
            // ②在微服务内部鉴权时，需要按照原数据格式返回，微服务的scope均为server
            // if (((DefaultOAuth2AccessToken) body).getScope().contains("server")) {
            // return body;
            // }
            return ResultMessage.createSuccessResult(body);
        } else if (body instanceof Map) {
            return ResultMessage.createFailedResult(body.toString());
        }
        return ResultMessage.createFailedResult(body.toString());
    }
}