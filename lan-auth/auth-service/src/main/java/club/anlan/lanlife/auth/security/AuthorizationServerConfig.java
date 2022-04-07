package club.anlan.lanlife.auth.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/4 17:17
 */
@Slf4j
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


    @Resource
    private DataSource dataSource;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private AuthUserDetailsServiceImpl authUserDetailsService;

    @Bean
    public RedisTokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Bean
    public TokenEnhancer tokenEnhancer(){
        return new TokenEnhancer() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
                                             OAuth2Authentication authentication) {
                // 自定义令牌声明，添加额外的属性
                if (accessToken instanceof DefaultOAuth2AccessToken){
                    DefaultOAuth2AccessToken token= (DefaultOAuth2AccessToken) accessToken;
                    Map<String, Object> additionalInformation = new LinkedHashMap<>();
                    additionalInformation.put("username",authentication.getName());
                    token.setAdditionalInformation(additionalInformation);
                    log.info("DefaultOAuth2AccessToken put username: {}", authentication.getName());
                }
                return accessToken;
            }
        };
    }

    @Bean
    public ClientDetailsService clientDetailsService(){
        return new JdbcClientDetailsService(dataSource);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 配置客户端详情
        clients.withClientDetails(clientDetailsService());
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenEnhancer(tokenEnhancer());
        endpoints.tokenStore(tokenStore());
        endpoints.authenticationManager(authenticationManager);
        //使用refresh_token额外配置,否则会报UserDetailsService is required
        endpoints.userDetailsService(authUserDetailsService);
    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        //允许表单认证
        oauthServer.checkTokenAccess("permitAll()");
        oauthServer.allowFormAuthenticationForClients();
    }

}

