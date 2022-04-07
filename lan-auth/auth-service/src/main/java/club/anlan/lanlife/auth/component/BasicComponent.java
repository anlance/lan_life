package club.anlan.lanlife.auth.component;

import club.anlan.lanlife.auth.client.BasicClient;
import club.anlan.lanlife.auth.dto.UserDto;
import club.anlan.lanlife.component.base.exception.BusinessException;
import club.anlan.lanlife.component.base.exception.BusinessRuntimeException;
import club.anlan.lanlife.component.base.result.ResultMessage;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 基础调用组件
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/3 23:23
 */
@Slf4j
@Component
public class BasicComponent {

    @Autowired
    private BasicClient basicClient;

    /**
     * 获取用户信息
     *
     * @param query 查询条件
     * @return 用户信息
     */
    public UserDto.User getUser(UserDto.Query query) {
        ResultMessage result = basicClient.getUser(query);
        if (ResultMessage.success(result)) {
            return JSON.parseObject(JSON.toJSONString(result.getData()), UserDto.User.class);
        }
        throw BusinessRuntimeException.createBusinessException(result.getErrMsg());
    }
}
