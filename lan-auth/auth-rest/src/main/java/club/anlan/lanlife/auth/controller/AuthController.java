package club.anlan.lanlife.auth.controller;

import club.anlan.lanlife.auth.vo.GetAuthTokenVO;
import club.anlan.lanlife.component.base.annotation.Log;
import club.anlan.lanlife.component.base.helper.RestTemplateHelper;
import club.anlan.lanlife.component.base.result.ResultMessage;
import club.anlan.lanlife.component.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * AuthController
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/4 18:30
 */
@Slf4j
@RestController()
public class AuthController {

    @Value("${security.oauth2.client.accessTokenUri:/auth/oauth/token}")
    private String accessTokenUrl;

    @Autowired
    @Qualifier("restClient3sService")
    private RestTemplateHelper restTemplateHelper;

    private static final String URL_TEMPLATE = "%s?grant_type=%s&scope=%s&client_id=%s&client_secret=%s";

    @Log(description = "获取token")
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResultMessage getAuthToken(@Validated @RequestBody GetAuthTokenVO getAuthTokenVO) {
        String url = String.format(URL_TEMPLATE, accessTokenUrl, getAuthTokenVO.getGrant_type(),
                getAuthTokenVO.getScope(), getAuthTokenVO.getClient_id(), getAuthTokenVO.getClient_secret());
        StringBuilder sb = new StringBuilder(url);
        if (StringUtil.isNotEmpty(getAuthTokenVO.getUsername())) {
            sb.append("&username=");
            sb.append(getAuthTokenVO.getUsername());
        }
        if (StringUtil.isNotEmpty(getAuthTokenVO.getPassword())) {
            sb.append("&password=");
            sb.append(getAuthTokenVO.getPassword());
        }
        String test = restTemplateHelper.postForm(sb.toString(), new HashMap<>());
        return JSON.parseObject(test, ResultMessage.class);
    }

}
