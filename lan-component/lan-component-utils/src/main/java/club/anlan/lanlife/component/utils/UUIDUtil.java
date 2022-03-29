package club.anlan.lanlife.component.utils;

import java.util.UUID;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2022/3/30 0:58
 */
public class UUIDUtil {

    /**
     * 取默认 UUID
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取默认 UUID
     */
    public static long getUuid() {
        long u = System.currentTimeMillis();
        String str = String.valueOf(Math.random());
        str = str.substring(2, str.length());
        String rlt = String.valueOf(u) + str;
        if (rlt.length() > 15) {
            rlt = rlt.substring(4, 19);
            try {
                Long.parseLong(rlt);
            } catch (Exception e) {
                rlt = rlt.substring(0, 14);
            }
        }
        return Long.parseLong(rlt);
    }

    /**
     * 获取指定长度 UUID
     *
     * @param length 指定长度
     */
    public static long getUuid(int length) {
        long u = System.currentTimeMillis();
        String str = String.valueOf(Math.random());
        str = str.substring(2, str.length());
        String rlt = String.valueOf(u) + str;
        if (rlt.length() > length) {
            rlt = rlt.substring(19 - length, 19);
            try {
                Long.parseLong(rlt);
            } catch (Exception e) {
                rlt = rlt.substring(0, length);
            }
        }
        return Long.parseLong(rlt);
    }
}
