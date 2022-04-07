package club.anlan.lanlife.auth.security;

import club.anlan.lanlife.auth.constant.Constant;
import club.anlan.lanlife.auth.security.encoder.CustomPasswordEncoder;
import club.anlan.lanlife.auth.security.encoder.PasswordEncoder;
import club.anlan.lanlife.component.base.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 重写DaoAuthenticationProvider,以便改变密码校验方式
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/4 14:35
 */
@Slf4j
public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private UserDetailsService userDetailsService;

    private CustomPasswordEncoder passwordEncoder;

    private String userNotFoundEncodedPassword;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials", Constant.ERROR_BAD_CREDENTIALS));
        }

        String presentedPassword = authentication.getCredentials().toString();
        if (!passwordEncoder.isPasswordValid(presentedPassword, userDetails.getPassword(), userDetails.getUsername())) {
            throw new BadCredentialsException(
                    messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", Constant.ERROR_BAD_CREDENTIALS));
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        try {
            UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        } catch (UsernameNotFoundException ex) {
            if (authentication.getCredentials() != null) {
                String presentedPassword = authentication.getCredentials().toString();
                passwordEncoder.isPasswordValid(userNotFoundEncodedPassword, presentedPassword, null);
            }
            throw ex;
        } catch (InternalAuthenticationServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    public void setPasswordEncoder(Object passwordEncoder) {
        AssertUtil.notNull(passwordEncoder, "passwordEncoder cannot be null");

        if (passwordEncoder instanceof CustomPasswordEncoder) {
            setPasswordEncoder((CustomPasswordEncoder) passwordEncoder);
            return;
        }

        if (passwordEncoder instanceof org.springframework.security.crypto.password.PasswordEncoder) {
            final org.springframework.security.crypto.password.PasswordEncoder delegate = (org.springframework.security.crypto.password.PasswordEncoder) passwordEncoder;
            setPasswordEncoder((PasswordEncoder) (encPass, rawPass, username) -> delegate.matches(rawPass, encPass));
            return;
        }

        throw new IllegalArgumentException("passwordEncoder must be a PasswordEncoder instance");
    }

    private void setPasswordEncoder(CustomPasswordEncoder passwordEncoder) {
        AssertUtil.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
    }

    protected CustomPasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }


}
