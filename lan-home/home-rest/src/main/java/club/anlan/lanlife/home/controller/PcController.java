package club.anlan.lanlife.home.controller;

import club.anlan.lanlife.component.base.annotation.Log;
import club.anlan.lanlife.component.base.result.ResultMessage;
import club.anlan.lanlife.home.util.ExecuteShellUtil;
import club.anlan.lanlife.home.util.PcUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统控制器
 *
 * @author lan
 * @version 1.0
 * @date 2021/9/11 17:32
 */
@RestController
@Slf4j
@RequestMapping("/pc")
public class PcController {

    @Log(description = "开机")
    @GetMapping("/start")
    public ResultMessage start() {
        PcUtil.start();
        return ResultMessage.createSuccessResult();
    }
}
