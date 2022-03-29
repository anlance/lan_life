package club.anlan.lanlife.component.base.util;


import java.io.*;

/**
 * Json 工具类
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/5 16:47
 */
public class JsonUtil {

    public static String readJsonFile(InputStream inputStream) {
        String jsonStr = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
