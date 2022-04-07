package club.anlan.lanlife.component.base.exception;

import club.anlan.lanlife.component.base.constant.Constants;
import club.anlan.lanlife.component.base.constant.Regexp;
import club.anlan.lanlife.component.base.i18n.MessageResource;
import club.anlan.lanlife.component.base.spring.AppContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * checked异常基础
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/24 13:34
 */
@Slf4j
@Getter
@Setter
class BasicException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * 错误信息
     */
    private String errMsg;

    /**
     * 错误代码
     */
    private String code;

    public BasicException(String i18nCode, Object... params) {
        super();
        try {
            MessageResource messageSource = AppContext.getBean(MessageResource.class);
            String i18nMsg = null;
            if (params != null) {
                i18nMsg = messageSource.getMessageWithParams(i18nCode, params);
            } else {
                i18nMsg = messageSource.getMessage(i18nCode);
            }
            if (StringUtils.isNotBlank(i18nMsg)) {
                int index = i18nMsg.indexOf(":");
                if (index > 0) {
                    String msgCode = i18nMsg.substring(0, index);
                    Pattern pattern = Pattern.compile(Regexp.ERROR_CODE_PATTERN);
                    Matcher matcher = pattern.matcher(msgCode);
                    // 需满足错误编码格式
                    if (matcher.matches()) {
                        String msgDetail = i18nMsg.substring(index + 1);
                        this.setCode(msgCode);
                        this.setErrMsg(msgDetail);
                    }
                }
                if (StringUtils.isBlank(code)) {
                    this.setCode(Constants.FAILED_CODE);
                    this.setErrMsg(i18nMsg);
                }
            } else {
                this.setCode(Constants.FAILED_CODE);
                this.setErrMsg(i18nCode);
            }
        } catch (Exception e) {
            log.error("exception: {}", e);
            this.setCode(Constants.FAILED_CODE);
            this.setErrMsg(i18nCode);
        }
    }

    public BasicException(String errMsg, Throwable cause) {
        super(errMsg, cause);
        setErrMsg(errMsg);
    }

    public BasicException(String code, String errMsg) {
        super(errMsg);
        setErrMsg(errMsg);
        setCode(code);
    }

    public BasicException(String code, String errMsg, Throwable cause) {
        super(errMsg, cause);
        setErrMsg(errMsg);
        setCode(code);
    }

}