package club.anlan.lanlife.component.utils.collection;

import club.anlan.lanlife.component.utils.ReflectionUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import java.util.*;

/**
 * 集合工具类
 *
 * @author lan
 * @version 1.0
 * @date 2022/3/29 1:21
 */
public class CollectionUtil {

    private CollectionUtil() {
    }

    public static boolean isEmpty(Collection<?> collection) {
        return CollectionUtils.isEmpty(collection);
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return CollectionUtils.isNotEmpty(collection);
    }


    /**
     * 将集合转为map，key为指定的属性值，value为集合中的单个对象
     */
    public static Map<?, ?> convertListToTreeMap(Collection<?> conllections, String keyName) {
        if (conllections == null || conllections.isEmpty()) {
            return null;
        }
        try {
            Map<Object, Object> map = new LinkedHashMap<>();
            for (Object obj : conllections) {
                map.put(ReflectionUtil.getMethodValueForField(obj, keyName), obj);
            }
            return map;
        } catch (SecurityException e) {
            //SecurityException异常，返回null
            return null;
        }
    }

    /**
     * Map反转，将value作为key，key作为value
     */
    public static Map<String, String> reverMap(Map<String, String> map) {
        Map<String, String> reverMap = new HashMap<>();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                reverMap.put(entry.getValue(), entry.getKey());
            }
        }
        return reverMap;
    }


}
