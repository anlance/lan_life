package club.anlan.lanlife.auth.util;

import club.anlan.lanlife.component.utils.security.MD5Util;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/5 12:03
 */
public class Md5UtilTest {

    public static void main(String[] args) {
        System.out.println(MD5Util.md5Hex(MD5Util.md5Hex("GSSG255211") + "master"));
        System.out.println(MD5Util.md5Hex("GSSG255211"));
        System.out.println(MD5Util.md5Hex("2247a9179de897783ea1bc7e169db34e" + "uiNZIu78U3"));
        System.out.println(MD5Util.md5Hex("faker"));
    }
}
