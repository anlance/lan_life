package club.anlan.lanlife.component.redis.auth;

import club.anlan.lanlife.component.base.helper.RestClient3sService;
import club.anlan.lanlife.component.base.result.ResultMessage;
import club.anlan.lanlife.component.redis.util.RedisStringUtil;
import club.anlan.lanlife.component.utils.StringUtil;
import club.anlan.lanlife.component.utils.constant.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/4 19:16
 */
@Slf4j
public class OAuth2FeignRequestInterceptor implements RequestInterceptor {


    private static final String BEARER = "Bearer %s";

    private static final String TOKEN_REDIS_KEY = "oauth:token:server:%s";

    private static final String URL_TEMPLATE = "%s?grant_type=%s&scope=%s&client_id=%s&client_secret=%s";

    private RedisStringUtil redisStringUtil;

    private ClientCredentialsResourceDetails clientCredentialsResourceDetails;

    private RestTemplate restTemplate;

    public OAuth2FeignRequestInterceptor(RestTemplate restTemplate, RedisStringUtil redisStringUtil,
                                         ClientCredentialsResourceDetails clientCredentialsResourceDetails) {
        this.restTemplate = restTemplate;
        this.redisStringUtil = redisStringUtil;
        this.clientCredentialsResourceDetails = clientCredentialsResourceDetails;
    }

    @Override
    public void apply(RequestTemplate template) {
        Set<String> keySet = template.headers().keySet();
        if (keySet.contains(Constants.AUTHORIZATION)) {
            return;
        }
        String token = redisStringUtil
                .get(String.format(TOKEN_REDIS_KEY, clientCredentialsResourceDetails.getClientId()));
        log.info("token cache: {}", token);
        if (StringUtil.isEmpty(token)) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            String url = String.format(URL_TEMPLATE, clientCredentialsResourceDetails.getAccessTokenUri(),
                    clientCredentialsResourceDetails.getGrantType(),
                    clientCredentialsResourceDetails.getScope().get(0),
                    clientCredentialsResourceDetails.getClientId(),
                    clientCredentialsResourceDetails.getClientSecret());

            String res = new RestClient3sService().postForm(url, new HashMap<>(), headers.toSingleValueMap());
            if (res != null) {
                ResultMessage resultMessage = JSON.parseObject(res, ResultMessage.class);
                if (ResultMessage.success(resultMessage)) {
                    JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(resultMessage.getData()));
                    token = jsonObject.getString("access_token");
                    redisStringUtil.add(
                            String.format(TOKEN_REDIS_KEY, clientCredentialsResourceDetails.getClientId()), token,
                            jsonObject.getIntValue("expires_in"), TimeUnit.SECONDS);

                }
            }
            log.info("New token: {}", token);
        }
        template.header(Constants.AUTHORIZATION, String.format(BEARER, token));
    }

}
