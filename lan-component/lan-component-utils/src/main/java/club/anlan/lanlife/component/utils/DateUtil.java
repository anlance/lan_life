package club.anlan.lanlife.component.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期/时间工具类
 *
 * @author lan
 * @version 1.0
 * @date 2022/3/29 1:12
 */
@Slf4j
public class DateUtil extends DateUtils {

    public static final int FIRST_DAY_OF_WEEK = Calendar.MONDAY;
    public static final String DATA_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATA_FORMAT_YYYY_MM = "yyyy-MM";
    public static final String DATA_FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String DATA_FORMAT_YYYYMM = "yyyyMM";
    public static final String DATA_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATA_FORMAT_MM_DD_HH_MM_SS = "MM-dd HH:mm:ss";
    public static final String DATA_FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String DATA_FORMAT_AMERICAN = "MMM dd, yyyy hh:mm:ss a";
    public static final String ISO_DATETIME_TIME_ZONE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String ISO_DATETIME_TIME_ZONE_FORMAT_LC = "yyyyMMdd'T'HHmmssSSS'Z'";
    public static final String ISO_DATETIME_TIME_ZONE_FORMAT_LC_NO_MS = "yyyyMMdd'T'HHmmss'Z'";
    public static final String ISO_DATETIME_TIME_ZONE_FORMAT_LC_NO_MSZ = "yyyyMMdd'T'HHmmss";
    public static final String ISO_DATETIME_TIME_ZONE_FORMAT_YMDHMS = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String DATA_FORMAT_MMM_DD_YYYY_HH_MM_SS_AAA = "MMM dd,yyyy HH:mm:ss aaa";
    public static final String DATA_FORMAT_MM_DD = "MM-dd";
    public static final String DATA_FORMAT_YMDHMS = "yyyyMMddHHmmss";
    public static final String DATA_FORMAT_YMD_H_M_S = "yyyy/MM/dd HH:mm:ss";
    public static final String DATA_FORMAT_H_M_S = "HH:mm:ss";
    public static final String DATA_FORMAT_YMD = "yyyy/MM/dd";
    public static final String DATA_FORMAT_YM = "yyyy/MM";
    public static final String DATA_FORMAT_Y = "yyyy年";
    public static final String DATA_FORMAT_DD = "dd";

    public static final String THE = "第";
    public static final String YEAR = "年";
    public static final String MONTH = "月";
    public static final String WEEK = "周";
    public static final String DAY = "日";
    public static final String HOUR = "时";
    public static final String MINUTE = "分";
    public static final String SECOND = "秒";
    public static final String DAY_TI = "天";

    public static final Object STRING_ZERO = "0";

    /**
     * HH:mm:ss
     */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    /**
     * HH:mm
     */
    public static final String DEFAULT_TIME_FORMAT_HM = "HH:mm";

    /**
     * 修改linux系统日期时间格式
     */
    public static final String DATE_FORMAT_FOR_LINUX = "yyyy.MM.dd-HH:mm:ss";

    public static final long DAY_SECOND = 24 * 3600 * 1000;

    /**
     * 格式化日期为默认格式：yyyy-MM-dd HH:mm:ss
     */
    public static String dateToString(Date dateValue) {
        return new SimpleDateFormat(DATA_FORMAT_YYYY_MM_DD_HH_MM_SS).format(dateValue);
    }

    /**
     * 将日期转换为指定格式的字符串
     */
    public static String dateToString(Date dateValue, String strFormat) {
        return new SimpleDateFormat(strFormat).format(dateValue);
    }

    /**
     * 根据格式将字符串转换为日期对象
     */
    public static Date stringToDate(String strValue, String strFormat) {
        Date date = null;
        try {
            date = new SimpleDateFormat(strFormat).parse(strValue);
        } catch (ParseException e) {
            log.error("时间解析失败", e);
        }
        return date;
    }

    /**
     * Object类型的日期 转换为String
     */
    public static String objectToString(Object obj, String strFormat) {
        String result = null;
        if (obj != null) {
            if (obj instanceof Date) {
                result = dateToString((Date) obj, strFormat);
            } else {
                result = obj.toString();
            }
        }
        return result;
    }

    /**
     * Date转化为Timestamp
     */
    public static Timestamp dateToTimestamp(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DATA_FORMAT_YYYY_MM_DD_HH_MM_SS);
        String time = df.format(date);
        return Timestamp.valueOf(time);
    }

    /**
     * 根据格式字符串将日期转化为Timestamp
     */
    public static Timestamp dateToTimestamp(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        String time = df.format(date);
        return Timestamp.valueOf(time);
    }

    /**
     * Date转化为yyyyMMdd格式的String
     */
    public static String dateToyyyyMMddString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DATA_FORMAT_YYYYMMDD);
        return df.format(date);
    }

    /**
     * 将时间戳转化为Date
     */
    public static Date timestampToDate(Long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.getTime();
    }
}
