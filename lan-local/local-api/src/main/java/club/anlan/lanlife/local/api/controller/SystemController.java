package club.anlan.lanlife.local.api.controller;

import club.anlan.lanlife.base.annotation.Log;
import club.anlan.lanlife.base.result.ResultMessage;
import club.anlan.lanlife.local.system.utils.ExecuteShellUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统端 控制器
 *
 * @author lan
 * @version 1.0
 * @date 2021/8/8 21:42
 */
@RestController
@Slf4j
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private ExecuteShellUtil executeShellUtil;


    @Log(description = "执行脚本")
    @GetMapping("/execute")
    public ResultMessage execute(@RequestParam(value = "command", required = false) String command) {
        return ResultMessage.createSuccessResult(executeShellUtil.executeForResult(command));
    }
}
