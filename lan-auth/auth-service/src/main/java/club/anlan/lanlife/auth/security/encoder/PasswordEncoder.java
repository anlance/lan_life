package club.anlan.lanlife.auth.security.encoder;

/**
 * 密码校验器
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/4 15:12
 */
public interface PasswordEncoder {
    /**
     * 校验密码是否证券
     *
     * @param encPass 数据库中加密的密码
     * @param rawPass 前端传输的密码
     * @param username    用户名
     * @return true ? 密码正确 : 密码错误
     */
    boolean isPasswordValid(String encPass, String rawPass, String username);
}
