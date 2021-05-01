package club.anlan.lanlife.base.result;

import club.anlan.lanlife.base.constant.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 通用返回参数
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/4 23:07
 */
@Data
@AllArgsConstructor
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
     *
     * @return
     */
    public static ResultMessage createSuccessResult() {
        return new ResultMessage(true, Constants.SUCCESS_CODE, Constants.SUCCESS, Constants.BLANK_OBJECT);
    }

    /**
     * 创建成功状态返回结果
     *
     * @param data
     * @return
     */
    public static ResultMessage createSuccessResult(Object data) {
        return new ResultMessage(true, Constants.SUCCESS_CODE, Constants.SUCCESS, data);
    }

    /**
     * 创建错误返回结果消息
     *
     * @param code
     * @return
     */
    public static ResultMessage createFailedByCode(String code) {
        return new ResultMessage(false, code, Constants.FAILED, Constants.BLANK_OBJECT);
    }

    /**
     * 创建错误返回结果消息
     *
     * @param errMsg
     * @return
     */
    public static ResultMessage createFailedResult(String errMsg) {
        return new ResultMessage(false, Constants.FAILED_CODE, errMsg, Constants.BLANK_OBJECT);
    }

    /**
     * 创建错误返回结果消息
     *
     * @param code
     * @param errMsg
     * @return
     */
    public static ResultMessage createFailedResult(String code, String errMsg) {
        return new ResultMessage(false, code, errMsg, Constants.BLANK_OBJECT);
    }

}
