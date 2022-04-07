package club.anlan.lanlife.basic.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户信息
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/3 22:34
 */
@Data
public class UserDto {

    @Getter
    @Setter
    public static class Query {
        /**
         * 用户名
         */
        private String loginName;
    }

    @Getter
    @Setter
    public static class User {

        private String id;

        /**
         * 用户名
         */
        private String loginName;

        /**
         * 密码
         */
        private String loginPass;

        /**
         * 盐
         */
        private String salt;

        /**
         * 登录 IP
         */
        private String lastLoginIp;
    }
}
