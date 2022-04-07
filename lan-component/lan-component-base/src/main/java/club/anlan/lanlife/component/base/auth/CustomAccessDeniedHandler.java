package club.anlan.lanlife.component.base.auth;

import club.anlan.lanlife.component.base.constant.Constants;
import club.anlan.lanlife.component.base.constant.Regexp;
import club.anlan.lanlife.component.base.i18n.I18nEnum;
import club.anlan.lanlife.component.base.i18n.MessageResource;
import club.anlan.lanlife.component.base.result.ResultMessage;
import club.anlan.lanlife.component.base.spring.AppContext;
import club.anlan.lanlife.component.utils.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CustomAccessDeniedHandler
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/4 19:02
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        ObjectMapper objectMapper = new ObjectMapper();
        ResultMessage resultMessage = ResultMessage
                .createFailedByCode(Constants.FAILED_CODE);
        MessageResource messageSource = AppContext.getBean(MessageResource.class);
        String i18nMsg = messageSource.getMessage(I18nEnum.SYSTEM_ERROR, request.getLocale());
        if (StringUtil.isNotEmpty(i18nMsg)) {
            int index = i18nMsg.indexOf(":");
            if (index > 0) {
                String msgCode = i18nMsg.substring(0, index);
                Pattern pattern = Pattern.compile(Regexp.ERROR_CODE_PATTERN);
                Matcher matcher = pattern.matcher(msgCode);
                // 需满足错误编码格式
                if (matcher.matches()) {
                    resultMessage.setCode(msgCode);
                    resultMessage.setErrMsg(i18nMsg.substring(index + 1));
                }
            }
        }
        objectMapper.writeValue(response.getOutputStream(), resultMessage);
    }

}
