package club.anlan.lanlife.basic.vo;

import club.anlan.lanlife.basic.domain.User;
import club.anlan.lanlife.component.utils.security.MD5Util;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户 vo
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/3 20:53
 */
@Data
public class UserVo {

    @Getter
    @Setter
    public static class AddVo {

        /**
         * 用户名
         */
        private String loginName;

        /**
         * 密码加密盐值
         */
        private String salt;

        /**
         * 密码
         */
        private String loginPass;

        /**
         * addVo 转化为 user 对象
         *
         * @return 用户
         */
        public User toUser() {
            User user = new User();
            user.setLoginName(this.loginName);
            user.setSalt(this.salt);
            user.setLoginPass(MD5Util.md5Hex(MD5Util.md5Hex(this.loginName) + this.salt));
            return user;
        }

    }


    @Getter
    @Setter
    public static class UpdateVo {

        private String id;

        /**
         * 密码
         */
        private String loginPass;

        /**
         * 用户邮箱
         */
        private String userEmail;

        /**
         * 用户头像地址
         */
        private String userPic;

        /**
         * 描述
         */
        private String memo;
    }
}
