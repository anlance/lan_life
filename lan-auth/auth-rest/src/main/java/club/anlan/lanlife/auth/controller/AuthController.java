package club.anlan.lanlife.auth.controller;

import club.anlan.lanlife.auth.component.BasicComponent;
import club.anlan.lanlife.auth.dto.UserDto;
import club.anlan.lanlife.auth.service.UserService;
import club.anlan.lanlife.auth.vo.GetAuthTokenVO;
import club.anlan.lanlife.component.base.annotation.Log;
import club.anlan.lanlife.component.base.enums.ClientType;
import club.anlan.lanlife.component.base.helper.RestTemplateHelper;
import club.anlan.lanlife.component.base.result.ResultMessage;
import club.anlan.lanlife.component.redis.cache.TokenUserNameCache;
import club.anlan.lanlife.component.redis.cache.UserSessionCache;
import club.anlan.lanlife.component.redis.cache.UserSessionInfo;
import club.anlan.lanlife.component.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private TokenUserNameCache tokenUserNameCache;

    @Autowired
    private UserSessionCache userSessionCache;

    @Autowired
    private BasicComponent basicComponent;

    @Autowired
    private UserService userService;

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
        ResultMessage res = JSON.parseObject(restTemplateHelper.postForm(sb.toString(), new HashMap<>()), ResultMessage.class);
        initUserSessionInfo(res);
        return res;
    }

    /**
     * 初始化 userSessionInfo
     * @param res 获取 token 结果
     */
    private void initUserSessionInfo(ResultMessage res){
        if (Objects.nonNull(res) && Objects.nonNull(res.getData())) {
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(res.getData()));
            String token = jsonObject.getString("access_token");
            String username = jsonObject.getString("username");
            long curExpiredSecond = userService.getExpiredTime(ClientType.BS);
            UserDto.User user = basicComponent.getUser(UserDto.Query.valueOf(username));
            if(StringUtil.isNotEmpty(token) && Objects.nonNull(user)) {
                UserSessionInfo userSessionInfo = new UserSessionInfo();
                userSessionInfo.setUserId(user.getId());
                userSessionInfo.setUserName(user.getUsername());
                userSessionInfo.setClientType(ClientType.BS);
                userSessionInfo.setLoginTime(new Date());
                userSessionInfo.setToken(token);
                userSessionInfo.setLastRefreshTime(System.currentTimeMillis());
                // redis缓存
                tokenUserNameCache.add(token, username, curExpiredSecond, TimeUnit.SECONDS);
                userSessionCache.add(username, userSessionInfo, curExpiredSecond, TimeUnit.SECONDS);
                log.info("userInfo: {}", userSessionInfo);
            }

        }

    }

}
