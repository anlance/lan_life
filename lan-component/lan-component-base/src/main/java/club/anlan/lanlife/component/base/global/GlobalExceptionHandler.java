package club.anlan.lanlife.component.base.global;

import club.anlan.lanlife.component.base.constant.Constants;
import club.anlan.lanlife.component.base.constant.Regexp;
import club.anlan.lanlife.component.base.enums.ApiResponseCodeEnum;
import club.anlan.lanlife.component.base.exception.BusinessException;
import club.anlan.lanlife.component.base.exception.BusinessRuntimeException;
import club.anlan.lanlife.component.base.i18n.I18nEnum;
import club.anlan.lanlife.component.base.i18n.MessageResource;
import club.anlan.lanlife.component.base.result.ResultMessage;
import club.anlan.lanlife.component.utils.StringUtil;
import club.anlan.lanlife.component.utils.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 全局异常处理
 *
 * @author lan
 * @version 1.0
 * @date 2022/3/29 0:44
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    private MessageResource messageSource;


    @ExceptionHandler(value = BusinessRuntimeException.class)
    @ResponseBody
    public ResultMessage handleBusinessRuntimeException(BusinessRuntimeException exception) {
        return ResultMessage.createFailedResult(exception.getCode(), exception.getErrMsg());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResultMessage handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<ObjectError> list = exception.getBindingResult().getAllErrors();
        String errorShow;
        if (CollectionUtil.isEmpty(list)) {
            errorShow = messageSource.getMessage(I18nEnum.ERROR_PARAMETER_VALIDATE);
        } else {
            //默认报第一个参数错误
            String i18nCode = list.get(0).getDefaultMessage();
            errorShow = messageSource.getMessage(i18nCode);
        }
        return ResultMessage.createFailedResult(ApiResponseCodeEnum.PARAMETER_ERROR, errorShow);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultMessage defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws Exception {
        // 错误码
        String errorCode = null;
        // 返回页面错误信息
        String errorShow = null;
        // 打印错误提示信息
        String printMsg = null;
        // 是否打印堆栈信息
        boolean printStack = true;
        String i18nMsg = null;

        if (e instanceof BusinessRuntimeException) {
            errorCode = ((BusinessRuntimeException) e).getCode();
            errorShow = ((BusinessRuntimeException) e).getErrMsg();
            printStack = false;
        } else if (e instanceof BusinessException) {
            errorCode = ((BusinessException) e).getCode();
            errorShow = ((BusinessException) e).getErrMsg();
            printStack = false;
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException me = ((MethodArgumentNotValidException) e);
            List<ObjectError> list = me.getBindingResult().getAllErrors();
            if (CollectionUtil.isEmpty(list)) {
                errorShow = messageSource.getMessage(I18nEnum.ERROR_PARAMETER_TYPE, request.getLocale());
            } else {
                String i18nCode = list.get(list.size() - 1).getDefaultMessage();
                i18nMsg = messageSource.getMessage(i18nCode, request.getLocale());
            }
            printStack = false;
        } else if (e instanceof BindException) {
            BindException be = ((BindException) e);
            List<ObjectError> list = be.getBindingResult().getAllErrors();
            if (CollectionUtil.isNotEmpty(list)) {
                String i18nCode = list.get(list.size() - 1).getDefaultMessage();
                i18nMsg = messageSource.getMessage(i18nCode, request.getLocale());
                //i18n消息和i18n Code相同，则说明可能没有配置i18n的code，返回用户也无意义，直接返回参数校验失败
                if (Objects.equals(i18nMsg, i18nCode)) {
                    i18nMsg = messageSource.getMessage(I18nEnum.ERROR_PARAMETER_VALIDATE);
                }
            }
        } else if (e instanceof RuntimeException) {
            printMsg = I18nEnum.SYSTEM_ERROR.getKey();
        } else {
            printMsg = I18nEnum.SYSTEM_ERROR.getKey();
        }
        if (errorShow == null) {
            if (StringUtil.isEmpty(i18nMsg)) {
                i18nMsg = messageSource.getMessage(I18nEnum.SYSTEM_ERROR, request.getLocale());
            }
            if (StringUtil.isNotEmpty(i18nMsg)) {
                int index = i18nMsg.indexOf(":");
                if (index > 0) {
                    String msgCode = i18nMsg.substring(0, index);
                    Pattern pattern = Pattern.compile(Regexp.ERROR_CODE_PATTERN);
                    Matcher matcher = pattern.matcher(msgCode);
                    // 需满足错误编码格式
                    if (matcher.matches()) {
                        errorCode = msgCode;
                        errorShow = i18nMsg.substring(index + 1);
                    }
                }
                if (StringUtil.isBlank(errorShow)) {
                    errorShow = i18nMsg;
                }
            }
        }
        if (printStack) {
            // 打印堆栈信息
            log.error(printMsg != null ? printMsg : errorShow, e);
        } else {
            log.error("Error message: ", errorShow);
        }
        return ResultMessage.createFailedResult(
                StringUtil.isNotEmpty(errorCode) ? errorCode : Constants.FAILED_CODE, errorShow);
    }
}
