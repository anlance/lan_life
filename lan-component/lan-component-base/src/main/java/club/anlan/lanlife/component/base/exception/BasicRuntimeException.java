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
 * checked异常基础类
 *
 * @author lan
 * @version 1.0
 * @date 2022/3/29 0:20
 */
@Slf4j
@Getter
@Setter
public class BasicRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 错误信息
     */
    protected String errMsg;

    /**
     * 错误代码
     */
    protected String code;

    BasicRuntimeException() {
    }

}
