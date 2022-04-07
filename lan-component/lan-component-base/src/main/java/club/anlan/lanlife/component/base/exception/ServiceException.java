package club.anlan.lanlife.component.base.exception;

import club.anlan.lanlife.component.base.enums.ApiResponseCodeEnum;
import club.anlan.lanlife.component.utils.StringUtil;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/4 15:45
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String code;
    private String message;

    public ServiceException(String message) {
        super(message);
        this.message = message;
        this.code = ApiResponseCodeEnum.SYSTEM_ERROR.getValue();
    }

    public ServiceException(Throwable throwable) {
        super(throwable);
    }

    public ServiceException(Throwable throwable, String template, Object... args) {
        super(throwable);
        this.message = StringUtil.formatByRegex(template, args);
        this.code = ApiResponseCodeEnum.SYSTEM_ERROR.getValue();
    }

    public ServiceException(String template, Object... args) {
        this.message = StringUtil.formatByRegex(template, args);
        this.code = ApiResponseCodeEnum.SYSTEM_ERROR.getValue();
    }

    public ServiceException(String message, Throwable throwable) {
        super(message, throwable);
        this.message = message;
        this.code = ApiResponseCodeEnum.SYSTEM_ERROR.getValue();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
