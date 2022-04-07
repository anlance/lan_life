package club.anlan.lanlife.component.base.exception;

import club.anlan.lanlife.component.base.enums.ApiResponseCodeEnum;
import club.anlan.lanlife.component.utils.StringUtil;

/**
 * 对象为空 异常
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/4 15:42
 */
public class NullException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String code;
    private String message;

    public NullException(String message) {
        super(message);
        this.message = message;
        this.code = ApiResponseCodeEnum.SYSTEM_ERROR.getValue();
    }

    public NullException(Throwable throwable) {
        super(throwable);
    }

    public NullException(Throwable throwable, String template, String... args) {
        super(throwable);
        this.message = StringUtil.formatByRegex(template, args);
        this.code = ApiResponseCodeEnum.SYSTEM_ERROR.getValue();
    }

    public NullException(String template, String... args) {
        this.message = StringUtil.formatByRegex(template, args);
        this.code = ApiResponseCodeEnum.SYSTEM_ERROR.getValue();
    }

    public NullException(String message, Throwable throwable) {
        super(throwable);
        this.message = message;
        this.code = ApiResponseCodeEnum.SYSTEM_ERROR.getValue();
    }

    public NullException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
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
