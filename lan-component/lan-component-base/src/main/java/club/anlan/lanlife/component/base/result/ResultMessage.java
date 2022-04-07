package club.anlan.lanlife.component.base.result;

import club.anlan.lanlife.component.base.constant.Constants;
import club.anlan.lanlife.component.base.constant.Regexp;
import club.anlan.lanlife.component.base.enums.ApiResponseCodeEnum;
import club.anlan.lanlife.component.base.i18n.MessageResource;
import club.anlan.lanlife.component.base.spring.AppContext;
import club.anlan.lanlife.component.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用返回参数
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/4 23:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultMessage<T> {

    private static final long serialVersionUID = 7629041048366102233L;

    /**
     * 是否成功
     */
    private boolean success = true;

    /**
     * 结果编码
     */
    private String code;

    /**
     * 错误消息
     */
    private String errMsg = "";

    /**
     * 返回数据
     */
    private T data;

    /**
     * 创建成功状态返回结果
     */
    public static ResultMessage createSuccessResult() {
        return new ResultMessage(true, Constants.SUCCESS_CODE, Constants.SUCCESS, Constants.BLANK_OBJECT);
    }

    /**
     * 创建成功状态返回结果
     */
    public static ResultMessage createSuccessResult(Object data) {
        return new ResultMessage(true, Constants.SUCCESS_CODE, Constants.SUCCESS, data);
    }

    /**
     * 创建错误返回结果消息
     */
    public static ResultMessage createFailedByCode(String code) {
        return new ResultMessage(false, code, Constants.FAILED, Constants.BLANK_OBJECT);
    }

    public static ResultMessage createFailedByI18nCode(String i18nCode, Object... params) {
        if (StringUtil.isEmpty(i18nCode)) {
            return new ResultMessage(false, Constants.FAILED_CODE, StringUtil.EMPTY, Constants.BLANK_OBJECT);
        }
        MessageResource messageSource = AppContext.getBean(MessageResource.class);
        String i18nMsg = null;
        if (i18nCode != null) {
            i18nMsg = messageSource.getMessageWithParams(i18nCode, params);
        } else {
            i18nMsg = messageSource.getMessage(i18nCode);
        }
        if (StringUtil.isNotEmpty(i18nMsg)) {
            int index = i18nMsg.indexOf(":");
            if (index > 0) {
                String msgCode = i18nMsg.substring(0, index);
                Pattern pattern = Pattern.compile(Regexp.ERROR_CODE_PATTERN);
                Matcher matcher = pattern.matcher(msgCode);
                // 需满足错误编码格式
                if (matcher.matches()) {
                    String msgDetail = i18nMsg.substring(index + 1);
                    return new ResultMessage(false, msgCode, msgDetail, Constants.BLANK_OBJECT);
                }
            } else {
                return new ResultMessage(false, Constants.FAILED_CODE, i18nMsg, Constants.BLANK_OBJECT);
            }
        }
        return new ResultMessage(false, Constants.FAILED_CODE, StringUtil.EMPTY, Constants.BLANK_OBJECT);
    }

    /**
     * 创建错误返回结果消息
     */
    public static ResultMessage createFailedResult(String errMsg) {
        return new ResultMessage(false, Constants.FAILED_CODE, errMsg, Constants.BLANK_OBJECT);
    }

    /**
     * 创建错误返回结果消息
     */
    public static ResultMessage createFailedResult(String code, String errMsg) {
        return new ResultMessage(false, code, errMsg, Constants.BLANK_OBJECT);
    }

    public static ResultMessage createFailedResult(ApiResponseCodeEnum codeEnum, String errMsg) {
        return new ResultMessage(false, codeEnum.getValue(), errMsg, Constants.BLANK_OBJECT);
    }

    /**
     * 请求是否成功
     */
    public static boolean success(ResultMessage resultMessage) {
        if (Objects.nonNull(resultMessage) && resultMessage.isSuccess()) {
            return true;
        }
        return false;
    }

}
