package club.anlan.lanlife.home.controller;

import club.anlan.lanlife.component.base.annotation.Log;
import club.anlan.lanlife.component.base.result.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试类
 *
 * @author lan
 * @version 1.0
 * @date 2023/12/24 16:39
 */
@RestController
@Slf4j
@RequestMapping("/template")
public class TemplateController {

    @Log(description = "等待N秒后返回")
    @GetMapping("/wait")
    public ResultMessage wait(@RequestParam(value = "second", required = false) int second) {
        try {
            Thread.sleep(second);
        } catch (Exception e) {
            log.error("sleep error, ", e);
        }
        return ResultMessage.createSuccessResult(second);
    }
}
