package club.anlan.lanlife.proxy.server.demo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @ExcelProperty(value = "用户id", index = 0)
    private Long id;
    @ExcelProperty(value = "账号", index = 1)
    private String userNo;
    @ExcelProperty(value = "姓名", index = 2)
    private String name;
    @ExcelProperty(value = "性别", index = 3)
    private Integer gender;
    @ExcelProperty(value = "出生日期", index = 4)
    private Date birthday;
    @ExcelProperty(value = "手机号", index = 5)
    private String phone;
    @ExcelProperty(value = "邮箱", index = 6)
    private String email;
    @ExcelIgnore
    private Integer isDelete;
    @ExcelProperty(value = "地址", index = 7)
    private String address;
}

