package club.anlan.lanlife.basic.util;

/**
 * 地理编码工具类
 *
 * @author lan
 * @version 1.0
 * @date 2021/12/27 0:10
 */
public class LoactionUtil {

    public static void main(String[] args) {
        String url = "https://restapi.amap.com/v3/geocode/regeo?key=a99c13add6019c305de02c3096ff616a&location=116.480881,39.989410";
        String key = "a99c13add6019c305de02c3096ff616a";
        String location = "116.480881,39.989410";
        url = url + "key=" + key + "&location=" + location;
    }
}
