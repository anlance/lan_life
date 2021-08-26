package club.anlan.lanlife.demo.controller;

import club.anlan.lanlife.demo.domain.File;
import club.anlan.lanlife.demo.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SimpleController
 *
 * @author lan
 * @version 1.0
 * @date 2021/8/27 0:10
 */
@RestController
@Slf4j
public class SimpleController {

    @Autowired
    private FileService fileService;

    @GetMapping("/test")
    public void test() {
        log.info("test: {}", fileService.insertFile(new File()));
    }
}
