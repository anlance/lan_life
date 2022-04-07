package club.anlan.lanlife.component.utils.security;

import club.anlan.lanlife.component.utils.constant.Constants;
import org.springframework.util.DigestUtils;

import java.util.Objects;

/**
 * MD5 工具类
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/3 21:29
 */
public class MD5Util {

    public static final String md5Hex(String text){
        if (Objects.isNull(text)) {
            return Constants.EMPTY_STRING;
        }
        return md5Hex(text.getBytes());
    }

    private static String md5Hex(byte[] bytes) {
        return DigestUtils.md5DigestAsHex(bytes);
    }
}
