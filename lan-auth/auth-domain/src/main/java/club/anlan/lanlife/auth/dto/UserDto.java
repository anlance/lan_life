package club.anlan.lanlife.auth.dto;

import club.anlan.lanlife.component.base.JsonString;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

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

        public static Query valueOf(String loginName){
            Query query = new Query();
            query.setLoginName(loginName);
            return query;
        }
    }

    @Getter
    @Setter
    public static class User implements UserDetails, JsonString {

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

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public String getPassword() {
            return this.loginPass;
        }

        @Override
        public String getUsername() {
            return this.loginName;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
