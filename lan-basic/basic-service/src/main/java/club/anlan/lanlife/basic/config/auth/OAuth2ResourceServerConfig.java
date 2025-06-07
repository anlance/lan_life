//package club.anlan.lanlife.basic.config.auth;
//
//import club.anlan.lanlife.component.base.auth.AuthExceptionEntryPoint;
//import club.anlan.lanlife.component.base.auth.CustomAccessDeniedHandler;
//import club.anlan.lanlife.component.redis.auth.OAuth2FeignRequestInterceptor;
//import club.anlan.lanlife.component.redis.util.RedisStringUtil;
//import feign.Logger;
//import feign.RequestInterceptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
//import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.client.OAuth2RestTemplate;
//import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
//import org.springframework.web.client.RestTemplate;
//import sun.security.krb5.internal.CredentialsUtil;
//
///**
// * OAuth2ResourceServerConfig
// *
// * @author lan
// * @version 1.0
// * @date 2022/4/5 15:59
// */
//@Import({CredentialsUtil.class})
//@Configuration
//@EnableResourceServer
//@EnableConfigurationProperties
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableOAuth2Client
//public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {
//
//    @Autowired
//    private ResourceServerProperties sso;
//
//    @Autowired
//    private RedisStringUtil redisStringUtil;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Autowired
//    private AuthExceptionEntryPoint authExceptionEntryPoint;
//
//    @Autowired
//    private CustomAccessDeniedHandler customAccessDeniedHandler;
//
//    private String DEMO_RESOURCE_ID = "order";
//
//    @Bean
//    @ConfigurationProperties(prefix = "security.oauth2.client")
//    public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
//        return new ClientCredentialsResourceDetails();
//    }
//
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) {
//        resources.resourceId(DEMO_RESOURCE_ID).stateless(true);
//        resources.authenticationEntryPoint(authExceptionEntryPoint).accessDeniedHandler(customAccessDeniedHandler);
//    }
//
//    @Bean
//    public RequestInterceptor oauth2FeignRequestInterceptor() {
//        return new OAuth2FeignRequestInterceptor(restTemplate, redisStringUtil, clientCredentialsResourceDetails());
//    }
//
//    @Bean
//    public OAuth2RestTemplate clientCredentialsRestTemplate() {
//        return new OAuth2RestTemplate(clientCredentialsResourceDetails());
//    }
//
//    @Bean
//    public ResourceServerTokenServices tokenServices() {
//        return new UserInfoTokenServices(sso.getUserInfoUri(), sso.getClientId());
//    }
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/user/getSalt", "**/inner/**", "/user/saveLocation","/menu/list").permitAll()
//                .antMatchers("/actuator/**").permitAll()
//                .anyRequest().authenticated()
//                .and().csrf().disable();
//    }
//
////    @Bean
////    Logger.Level feignLoggerLevel() {
////        return Logger.Level.FULL;
////    }
//}
