package club.anlan.lanlife.component.utils;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类
 *
 * @author lan
 * @version 1.0
 * @date 2022/3/29 1:26
 */
public class ReflectionUtil {

    /**
     * 设置对象内属性的值
     *
     * @param targetObject 类对象
     * @param name         属性名
     * @param value        属性值
     */
    public static void setFieldValue(Object targetObject, String name, Object value) {
        if (targetObject == null) {
            return;
        }
        Field field = ReflectionUtils.findField(targetObject.getClass(), name);
        if (field == null) {
            return;
        }
        setFieldValue(targetObject, field, value);
    }

    /**
     * 设置对象内属性的值
     *
     * @param targetObject 对象
     * @param field        经反射后生成的属性
     * @param value        属性值
     */
    public static void setFieldValue(Object targetObject, Field field, Object value) {
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, targetObject, value);
    }

    /**
     * 获取对象内属性的值
     *
     * @param targetObject 对象
     * @param field        经反射后生成的属性
     */
    public static Object getFieldValue(Object targetObject, Field field) {
        ReflectionUtils.makeAccessible(field);
        return ReflectionUtils.getField(field, targetObject);
    }

    /**
     * 获取对象内属性的值
     *
     * @param targetObject 对象
     * @param fieldName    属性名
     */
    public static Object getFieldValue(Object targetObject, String fieldName) {
        return getFieldValue(targetObject, getField(targetObject.getClass(), fieldName));
    }

    public static Field getField(Object targetObject, String fieldName) {
        return getField(targetObject.getClass(), fieldName);
    }

    /**
     * 根据 属性名 生成 getMethod返回的值，如果 找不到 get方法，则去找 is方法，
     * 如 on属性，如果找不到 getOn方法，会去找 isOn方法
     */
    public static Object getMethodValueForField(Object targetObject, String fieldName) {
        Method method = getMethodForField(targetObject.getClass(), fieldName);
        return ReflectionUtils.invokeMethod(method, targetObject);
    }

    /**
     * 根据 属性名 生成 getMethod，如果 找不到 get方法，则去找 is方法，
     * 如 on属性，如果找不到 getOn方法，会去找 isOn方法
     *
     */
    public static Method getMethodForField(Class<?> clazz, String fieldName) {
        String capitalizedFieldName = StringUtil.capitalize(fieldName);
        Method method = ReflectionUtils.findMethod(clazz, "get" + capitalizedFieldName);
        if (method == null) {
            method = ReflectionUtils.findMethod(clazz, "is" + capitalizedFieldName);
        }

        if (method == null) {
            throw new RuntimeException("类" + clazz + "中找不到" + fieldName + "的get或 is方法");
        }
        return method;
    }

    /**
     * 根据 属性名 生成 setMethod，如果 找不到 set方法，则去找 is方法，
     */
    @SuppressWarnings({"rawtypes"})
    public static Method setMethodForField(Class<?> clazz, String fieldName) {
        Method method = null;
        try {
            String capitalizedFieldName = StringUtil.capitalize(fieldName);
            Class[] parameterTypes = new Class[1];
            Field field = clazz.getDeclaredField(fieldName);
            parameterTypes[0] = field.getType();
            method = ReflectionUtils.findMethod(clazz, "set" + capitalizedFieldName, parameterTypes);
            if (method == null) {
                method = ReflectionUtils.findMethod(clazz, "is" + capitalizedFieldName);
            }

            if (method == null) {
                throw new RuntimeException("类" + clazz + "中找不到" + fieldName + "的set方法");
            }
        } catch (Exception e) {

        }
        return method;
    }


    /**
     * 根据 属性名 生成 setMethod，如果 找不到 set方法，则去找 is方法，
     * 注意，使用该方法时 必须保证 有一个 相应的 get方法，或 定义一个 相应的属性
     * 最后通过调用此方法进行赋值
     */
    public static Object setMethodValueForField(Object targetObject, String fieldName, Object value) {
        Method method = setMethodForField(targetObject.getClass(), fieldName);
        return ReflectionUtils.invokeMethod(method, targetObject, value);
    }

    /**
     * 通过反射生成field
     */
    public static Field getField(Class<?> clazz, String name) {
        return ReflectionUtils.findField(clazz, name);
    }
}
