package club.anlan.lanlife.component.base.exception;

import club.anlan.lanlife.component.base.constant.Regexp;
import club.anlan.lanlife.component.base.enums.ApiResponseCodeEnum;
import club.anlan.lanlife.component.base.i18n.I18n;
import club.anlan.lanlife.component.base.i18n.MessageResource;
import club.anlan.lanlife.component.base.spring.AppContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 业务层运行时异常类
 *
 * @author lan
 * @version 1.0
 * @date 2022/3/29 0:20
 */
@Slf4j
public class BusinessRuntimeException extends BasicRuntimeException {

    private static final long serialVersionUID = 1L;


    public static BusinessRuntimeException createI18nException(ApiResponseCodeEnum codeEnum, I18n i18n, Object... i18nParams) {
        return new BusinessRuntimeException(codeEnum, i18n, null, i18nParams);
    }

    public static BusinessRuntimeException createI18nBusinessException(I18n i18n, Object... i18nParams) {
        return new BusinessRuntimeException(ApiResponseCodeEnum.BUSINESS_ERROR, i18n, null, i18nParams);
    }

    public static BusinessRuntimeException createBusinessException(String errMsg) {
        return new BusinessRuntimeException(ApiResponseCodeEnum.BUSINESS_ERROR, null, errMsg, null);
    }

    private BusinessRuntimeException(ApiResponseCodeEnum codeEnum, I18n i18n, String errMsg, Object... i18nParams) {
        MessageResource messageSource = AppContext.getBean(MessageResource.class);
        if (Objects.isNull(messageSource)) {
            log.warn("获取MessageResource失败");
            this.code = codeEnum.getValue();
            this.errMsg = codeEnum.getName();
            return;
        }
        String i18nMsg;
        if (i18nParams != null && i18nParams.length > 0) {
            i18nMsg = messageSource.getMessageWithParams(i18n.getKey(), i18nParams);
        } else if (i18n != null) {
            i18nMsg = messageSource.getMessage(i18n.getKey());
        } else {
            i18nMsg = errMsg;
        }
        int index = i18nMsg.indexOf(":");
        if (index > 0) {
            String msgCode = i18nMsg.substring(0, index);
            Pattern pattern = Pattern.compile(Regexp.ERROR_CODE_PATTERN);
            Matcher matcher = pattern.matcher(msgCode);
            // 需满足错误编码格式
            if (matcher.matches()) {
                String msgDetail = i18nMsg.substring(index + 1);
                this.code = msgCode;
                this.errMsg = msgDetail;
                return;
            }
        }
        this.code = codeEnum.getValue();
        this.errMsg = i18nMsg;
    }
}
