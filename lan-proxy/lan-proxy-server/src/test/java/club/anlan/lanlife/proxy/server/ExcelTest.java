package club.anlan.lanlife.proxy.server;

import club.anlan.lanlife.proxy.server.demo.User;
import club.anlan.lanlife.proxy.server.demo.UserExcelListener;
import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2024/4/11 18:04
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ExcelTest {

  @Test
  public void testExcelRead() {
    //String fileName = "C:\\Users\\lan\\Desktop\\lan_life\\test_file\\test1.xlsx";
    String fileName = "C:\\Users\\lan\\Desktop\\lan_life\\test_file\\test2.xlsx";
    EasyExcel.read(fileName, User.class, new UserExcelListener()).sheet().doRead();
    // 如果excel是多行表头比如说2行，需要设置行头数headRowNumber，默认不设置为1行表头，sheet不传默认读取第一个sheet
//     EasyExcel.read(fileName, User.class, new UserExcelListener()).sheet().headRowNumber(2).doRead();
   }
}
