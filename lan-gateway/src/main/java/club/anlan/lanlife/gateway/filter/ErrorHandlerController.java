package club.anlan.lanlife.gateway.filter;

import club.anlan.lanlife.component.base.exception.BusinessRuntimeException;
import club.anlan.lanlife.component.base.result.ResultMessage;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/30 22:58
 */
@RestController
@Slf4j
public class ErrorHandlerController implements ErrorController {


    private static final String ERROR_PATH = "/error";

    private static final String SYSTEM_BUSY = "服务器繁忙，请稍后再试";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @RequestMapping(ERROR_PATH)
    public ResultMessage errorHandler(HttpServletRequest request) {
        final Integer code = (Integer) request.getAttribute("javax.servlet.error.status_code");
        final Throwable exception = (Throwable) request.getAttribute("javax.servlet.error.exception");
        if (exception instanceof ZuulException) {
            final Throwable cause = exception.getCause();
            if (cause instanceof BusinessRuntimeException) {
                log.error("网关路由失败,url={},code={},message={}", request.getRequestURI(), code, ((BusinessRuntimeException) cause).getErrMsg());
                return ResultMessage.createFailedResult(((BusinessRuntimeException) cause).getErrMsg());
            }
        }
        log.error("网关路由失败,url={},code={}", request.getRequestURI(), code, exception);
        return ResultMessage.createFailedResult(SYSTEM_BUSY);
    }

}
