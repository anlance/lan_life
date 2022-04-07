package club.anlan.lanlife.component.utils;

import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理包装工具类
 *
 * @author lan
 * @version 1.0
 * @date 2022/3/29 1:05
 */
public class StringUtil {

    private final static String DEFAULT_DELIMITER = "\\{}";
    private final static Pattern PATTERN = Pattern.compile(DEFAULT_DELIMITER);

    public final static String EMPTY = StringUtils.EMPTY;


    private StringUtil() {
    }

    /**
     * 字符串左侧补全
     */
    public static String padLeft(String str, String padStr, int length) {
        if (Objects.isNull(str) || Objects.isNull(padStr)) {
            return str;
        }
        return StringUtils.leftPad(str, length, padStr);
    }

    /**
     * 字符串移除
     */
    public static String remove(String str, String remove) {
        return StringUtils.remove(str, remove);
    }


    public static String removeEnd(String str, String remove) {
        return StringUtils.removeEnd(str, remove);
    }

    public static String removeEnd(String str) {
        if (isBlank(str)) {
            return str;
        }
        return str.substring(0, str.length() - 1);
    }

    /**
     * 产生随机数
     */
    public static String getSalt(int size) {
        Random random = new SecureRandom();
        byte[] salt = new byte[size];
        random.nextBytes(salt);
        return Base64Util.encode(salt);
    }

    /**
     * 生成随机数字
     */
    public static String generateRandomCode(int count) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 生成随机字符串
     */
    public static String generateRandomChars(int maxNum) {
        char[] arr = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
                't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9'};
        int length = arr.length;
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        int count = 0;
        while (count < maxNum) {
            sb.append(arr[r.nextInt(length)]);
            count++;
        }
        return sb.toString();
    }

    /**
     * 生成随机字符串(不包含数字0,1, 小写字母l,o, 大写字母I,L,O )
     */
    public static String generateRandomCharsWithoutConfusingItem(int maxNum) {
        char[] arr = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u',
                'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S',
                'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9'};
        int length = arr.length;
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        int count = 0;
        boolean hasNum = false;
        while (count < maxNum - 1) {
            char tempChar = arr[r.nextInt(length)];
            if (tempChar <= '9') {
                hasNum = true;
            }
            sb.append(tempChar);
            count++;
        }
        if (hasNum) {
            sb.append(arr[r.nextInt(length)]);
        } else {
            char[] number = {'2', '3', '4', '5', '6', '7', '8', '9'};
            sb.append(number[r.nextInt(number.length)]);
        }
        return sb.toString();
    }

    public static boolean isNotBlank(CharSequence source) {
        return StringUtils.isNotBlank(source);
    }

    public static boolean isBlank(CharSequence source) {
        return StringUtils.isBlank(source);
    }

    public static boolean isNotEmpty(CharSequence source) {
        return StringUtils.isNotEmpty(source);
    }

    public static boolean isEmpty(CharSequence source) {
        return StringUtils.isEmpty(source);
    }

    public static boolean equals(CharSequence cs1, CharSequence cs2) {
        return StringUtils.equals(cs1, cs2);
    }


    /**
     * 判断多个字符串是否全部为blank
     */
    public static Boolean isBlankBoth(String... params) {
        if ((null == params) || (params.length == 0)) {
            return true;
        }
        for (String item : params) {
            if (!StringUtils.isBlank(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断多个字符串是否至少一个为blank
     */
    public static Boolean isBlankLeastOne(String... params) {
        if ((null == params) || (params.length == 0)) {
            return true;
        }
        for (String item : params) {
            if (StringUtils.isBlank(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断多个字符串是否全部为empty
     */
    public static Boolean isEmptyBoth(String... params) {
        if ((null == params) || (params.length == 0)) {
            return true;
        }
        for (String item : params) {
            if (!StringUtils.isEmpty(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断多个字符串是否至少一个为empty
     */
    public static Boolean isEmptyLeastOne(String... params) {
        if ((null == params) || (params.length == 0)) {
            return true;
        }
        for (String item : params) {
            if (StringUtils.isEmpty(item)) {
                return true;
            }
        }
        return false;
    }


    public static String format(String pattern, Object... params) {
        if (StringUtils.isBlank(pattern) || null == params || params.length <= 0) {
            return pattern;
        }
        StringBuilder sb = new StringBuilder();
        int j = 0;

        for (int i = 0; i < pattern.length(); i++) {
            if (j < params.length) {
                if (pattern.charAt(i) == '{') {
                    if (i < pattern.length() - 1 && pattern.charAt(i + 1) == '}') {
                        sb.append(params[j]);
                        i++;
                        j++;
                        continue;
                    }
                }
            }
            sb.append(pattern.charAt(i));
        }
        return sb.toString();
    }

    public static String formatByRegex(String pattern, Object... params) {
        int len = params.length;
        if (isBlank(pattern) || len <= 0) {
            return pattern;
        }
        Matcher matcher = PATTERN.matcher(pattern);
        for (int i = 0; matcher.find(); i++) {
            if (i > len - 1) {
                return pattern;
            }
            String param = params[i] == null ? "null" : params[i].toString();
            pattern = PATTERN.matcher(pattern).replaceFirst("【" + param + "】");
        }
        return pattern;
    }


    /**
     * 生成导出Excel名称
     */
    public static String generateExcelFileName(String title) {
        if (StringUtil.isBlank(title)) {
            return title;
        }
        return title + DateUtil.dateToString(new Date(), DateUtil.DATA_FORMAT_YYYY_MM_DD);
    }

    public static String capitalize(String str){

        return StringUtils.capitalize(str);
    }

    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
        return StringUtils.equalsIgnoreCase(str1,str2);
    }

}
