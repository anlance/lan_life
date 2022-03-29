package club.anlan.lanlife.component.utils;

import club.anlan.lanlife.component.utils.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Base64Util 工具类
 *
 * @author lan
 * @version 1.0
 * @date 2022/3/29 1:07
 */
@Slf4j
public class Base64Util {
    /**
     * base64 编码
     *
     * @param data 待编码的数据
     * @return 编码后的数据
     */
    public static String encode(byte[] data) {
        byte[] enBytes = Base64.encodeBase64(data);
        return new String(enBytes, StandardCharsets.UTF_8);
    }

    /**
     * base64 解码
     *
     * @param data 待解码的数据
     * @return 解码后的数据
     */
    public static byte[] decode(String data) {
        return Base64.decodeBase64(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * base64 解码
     *
     * @param data 待解码的数据
     * @return 解码后的数据
     */
    public static String decodeToStr(String data) {
        byte[] deBytes = Base64.decodeBase64(data.getBytes(StandardCharsets.UTF_8));
        return new String(deBytes, StandardCharsets.UTF_8);
    }

    public static String fileToBase64(File file) {
        if (file == null || !file.exists()) {
            return Constants.EMPTY_STRING;
        }
        try {
            byte[] buffer = Files.readAllBytes(file.toPath());
            return encode(buffer);
        } catch (Exception e) {
            log.error("file转换为base64字符串失败", e);
        }
        return Constants.EMPTY_STRING;
    }
}
