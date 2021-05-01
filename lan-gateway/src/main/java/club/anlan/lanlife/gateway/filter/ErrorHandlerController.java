package club.anlan.lanlife.gateway.filter;

import club.anlan.lanlife.base.result.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/30 22:58
 */
@RestController
@Slf4j
public class ErrorHandlerController implements ErrorController {


    private static final String ERROR_PATH = "/error";

    // private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    // public ErrorHandlerController(ErrorAttributes errorAttributes) {
    // this.errorAttributes = errorAttributes;
    // }

    @RequestMapping(ERROR_PATH)
    public ResultMessage errorHandler(HttpServletRequest request) {
        // ServletWebRequest requestAttributes = new ServletWebRequest(request);
        // Map<String, Object> attr =
        // this.errorAttributes.getErrorAttributes(requestAttributes, false);
        log.error("网关路由失败: {}", request.getRequestURI());
        return ResultMessage.createFailedResult("ffffffeeeeeeee");
    }

}
