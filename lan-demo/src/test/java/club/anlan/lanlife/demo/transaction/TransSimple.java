package club.anlan.lanlife.demo.transaction;

import club.anlan.lanlife.demo.domain.File;
import club.anlan.lanlife.demo.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事务基本测试类
 *
 * @author lan
 * @version 1.0
 * @date 2021/8/26 0:18
 */
@Slf4j(topic = "INFO")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DisplayName("test transaction simple")
public class TransSimple {

    @Autowired
    private FileService fileService;

    private File file;

    @DisplayName("【事务】initFile")
    @BeforeEach
    void initFile() {
        file = new File();
        log.info("【init】-------------------------------------------------------------------------------------------");
    }

    @AfterEach
    @DisplayName("【事务】delete")
    void delete() {
        fileService.deleteFile(file.getId());
        log.info("【delete】-----------------------------------------------------------------------------------------");
    }

    @Test
    @Transactional
    @DisplayName("【事务】抛出异常，回滚")
    void testBack() {
        fileService.insertFile(file);
        Assertions.assertNotNull(fileService.getFile(file.getId()));
        Assertions.assertNull(fileService.getFile(file.getId()));
    }
}
