package club.anlan.lanlife.auth.vo;

import club.anlan.lanlife.auth.constant.Constant;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 获取 token vo
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/4 18:33
 */
@Data
public class GetAuthTokenVO {

    @NotEmpty(message = Constant.SYSTEM_ERROR_PARAMETER_VALIDATE)
    private String username;

    @NotEmpty(message = Constant.SYSTEM_ERROR_PARAMETER_VALIDATE)
    private String password;

    @NotEmpty(message = Constant.SYSTEM_ERROR_PARAMETER_VALIDATE)
    private String grant_type;

    @NotEmpty(message = Constant.SYSTEM_ERROR_PARAMETER_VALIDATE)
    private String scope;

    @NotEmpty(message = Constant.SYSTEM_ERROR_PARAMETER_VALIDATE)
    private String client_id;

    @NotEmpty(message = Constant.SYSTEM_ERROR_PARAMETER_VALIDATE)
    private String client_secret;
}
