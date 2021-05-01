package club.anlan.lanlife.base.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期/时间工具类
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/24 14:33
 */

public class DateUtil extends DateUtils {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static final int FIRST_DAY_OF_WEEK = Calendar.MONDAY;
    public static final String DATA_FORMAT_yyyy_MM_dd = "yyyy-MM-dd";
    public static final String DATA_FORMAT_yyyy_MM = "yyyy-MM";
    public static final String DATA_FORMAT_yyyyMMdd = "yyyyMMdd";
    public static final String DATA_FORMAT_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String DATA_FORMAT_MM_dd_HH_mm_ss = "MM-dd HH:mm:ss";
    public static final String DATA_FORMAT_yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
    public static final String DATA_FORMAT_AMERICAN = "MMM dd, yyyy hh:mm:ss a";
    public static final String ISO_DATETIME_TIME_ZONE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String ISO_DATETIME_TIME_ZONE_FORMAT_LC = "yyyyMMdd'T'HHmmssSSS'Z'";
    public static final String ISO_DATETIME_TIME_ZONE_FORMAT_LC_NO_MS = "yyyyMMdd'T'HHmmss'Z'";
    public static final String ISO_DATETIME_TIME_ZONE_FORMAT_LC_NO_MSZ = "yyyyMMdd'T'HHmmss";
    public static final String DATA_FORMAT_MMM_dd_yyyy_HH_mm_ss_aaa = "MMM dd,yyyy HH:mm:ss aaa";
    public static final String DATA_FORMAT_MM_dd = "MM-dd";
    public static final String DATA_FORMAT_YMDHMS = "yyyyMMddHHmmss";
    public static final String DATA_FORMAT_YMD_H_M_S = "yyyy/MM/dd HH:mm:ss";
    public static final String DATA_FORMAT_H_M_S = "HH:mm:ss";
    public static final String DATA_FORMAT_YMD = "yyyy/MM/dd";
    public static final String DATA_FORMAT_YM = "yyyy/MM";
    public static final String DATA_FORMAT_Y = "yyyy年";

    public static final String THE = "第";
    public static final String YEAR = "年";
    public static final String MONTH = "月";
    public static final String WEEK = "周";
    public static final String DAY = "日";
    public static final String HOUR = "时";
    public static final String MINITE = "分";
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
            logger.error("时间解析失败", e);
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
        SimpleDateFormat df = new SimpleDateFormat(DATA_FORMAT_yyyy_MM_dd_HH_mm_ss);
        String time = df.format(date);
        Timestamp ts = Timestamp.valueOf(time);
        return ts;
    }

    /**
     * 根据格式字符串将日期转化为Timestamp
     */
    public static Timestamp dateToTimestamp(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        String time = df.format(date);
        Timestamp ts = Timestamp.valueOf(time);
        return ts;
    }

    /**
     * Date转化为yyyyMMdd格式的String
     */
    public static String dateToyyyyMMddString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DATA_FORMAT_yyyyMMdd);
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

    /**
     * 获取今天的零点时刻00:00:00
     */
    public static Date getTodayBegin() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取今天的结束时刻23:59:59
     */
    public static Date getTodayEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);

        return cal.getTime();
    }

    /**
     * 获取某天的零点时刻00:00:00
     */
    public static Date getDayBegin(Date date) {
        if (date == null)
            date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取某天的结束时刻23:59:59
     */
    public static Date getDayEnd(Date date) {
        if (date == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 获取传入日期n天后的日期,如果传入日期为null，则表示当前日期n天后的日期
     */
    public static Date getAddDayDate(Date date, int plusDays) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.DAY_OF_MONTH, plusDays);
        return cal.getTime();
    }


    /**
     * 获取传入日期几天后（plusDays>0）or 几天前（plusDays<0）的指定格式的字符串日期
     */
    public static String getAddDayDateFromToday(Date date, int plusDays, String dateFormat) {
        return dateToString(getAddDayDate(date, plusDays), dateFormat);
    }

    /**
     * 获取指定时间再加上指定小时数后的日期,如果传入日期为null，则以当前时间计算
     */
    public static Date getAddHourDate(Date date, int plusHours) {
        if (date == null)
            date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, plusHours);

        return cal.getTime();
    }

    /**
     * 获取指定时间再加上指定分钟数后的日期,如果传入日期为null，则以当前时间计算
     */
    public static Date getAddMinuteDate(Date date, int plusMinutes) {
        if (date == null)
            date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, plusMinutes);

        return cal.getTime();
    }

    /**
     * 获取指定时间再加上指定秒数后的日期,如果传入日期为null，则以当前时间计算
     */
    public static Date getAddSecondDate(Date date, int plusMinutes) {
        if (date == null)
            date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, plusMinutes);

        return cal.getTime();
    }

    /**
     * 获取指定时间再加上指定月数后的日期,如果传入日期为null，则以当前时间计算
     */
    public static Date getAddMonthDate(Date date, int plusMonths) {

        if (date == null)
            date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, plusMonths);

        return cal.getTime();
    }

    /**
     * 获取指定时间再加上指定年数后的日期,如果传入日期为null，则以当前时间计算
     */
    public static Date getAddYearDate(Date date, int plusYears) {
        if (date == null)
            date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, plusYears);
        return cal.getTime();
    }

    /**
     * 获取指定时间再加上指定年数后的日期时间戳,如果传入日期为null，则以当前时间计算
     */
    public static Long getAddYearMillis(Long millis, int plusYears) {
        if (millis == null) {
            millis = System.currentTimeMillis();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        cal.add(Calendar.YEAR, plusYears);
        return cal.getTimeInMillis();
    }


    /**
     * 根据传入日期获取日期相应月的起始日期，eg：param:2011-11-10 12:10:50.999, return：2011-11-1 00:00:00.000,如果传入日期为null，则以当前时间计算
     *
     * @param date 日期
     * @return
     */
    public static Date getMonthBeginTime(Date date) {
        if (date == null)
            date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 根据传入日期获取日期相应月的截止日期，eg：param:2011-11-10 12:10:50.999, return：2011-11-30 23:59:59.999,如果传入日期为null，则以当前时间计算
     *
     * @param date 日期
     * @return
     */
    public static Date getMonthEndTime(Date date) {
        if (date == null)
            date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, -1);
        return cal.getTime();
    }

    public static Date getWeekBeginTime(Date date) {
        if (date == null) {
            date = new Date(System.currentTimeMillis());
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 根据传入日期获取日期相应月的截止日期，eg：param:2011-11-10 12:10:50.999, return：2011-11-30 23:59:59.999,如果传入日期为null，则以当前时间计算
     *
     * @param date 日期
     * @return
     */
    public static Date getWeekEndTime(Date date) {
        if (date == null) {
            date = new Date(System.currentTimeMillis());
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (cal.get(Calendar.WEEK_OF_MONTH) == cal.get(Calendar.DAY_OF_WEEK_IN_MONTH)) {
            cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, cal.get(Calendar.DAY_OF_WEEK_IN_MONTH));
        } else {
            cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) + 1);
        }
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 59);
        return cal.getTime();
    }


    /**
     * 获取都日期的最后日期 2015-12-31 59:59:59
     *
     * @param date 日期
     * @return
     */
    public static Date getYearEndTime(Date date) {
        if (date == null) {
            date = new Date(System.currentTimeMillis());
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, 12);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, -1);
        return cal.getTime();
    }

    /**
     * 获取都日期的起始日期 2018-01-01 00:00:00
     *
     * @param date 日期
     * @return
     */
    public static Date getYearStartTime(Date date) {
        if (date == null)
            date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
        cal.set(Calendar.MONTH, 12);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getYearEndTimeByYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, 12);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, -1);
        return cal.getTime();
    }

    public static Date getYearStartTimeByYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year - 1);
        cal.set(Calendar.MONTH, 12);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获得传入日期的年\月\日\小时\分,以整型数组方式返回
     *
     * @param date 日期
     * @return
     */
    public static int[] getTimeArray(Date date) {
        if (date == null) {
            date = new Date(System.currentTimeMillis());
        }
        int[] timeArray = new int[5];
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        timeArray[0] = cal.get(Calendar.YEAR);
        timeArray[1] = cal.get(Calendar.MONTH) + 1;
        timeArray[2] = cal.get(Calendar.DAY_OF_MONTH);
        timeArray[3] = cal.get(Calendar.HOUR_OF_DAY);
        timeArray[4] = cal.get(Calendar.MINUTE);
        return timeArray;
    }

    /**
     * 根据年月日得到Date类型时间
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date getDateByYMD(Integer year, Integer month, Integer day) {
        Calendar cal = Calendar.getInstance();
        if (year != null)
            cal.set(Calendar.YEAR, year);
        if (month != null)
            cal.set(Calendar.MONTH, month - 1);
        if (day != null)
            cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    /**
     * 计算两个日期间相隔的秒数，当第一个日期小于第二个日期时，是负数
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return
     */
    public static int getSecondBetween(Date date1, Date date2) {
        long m = date1.getTime();
        long n = date2.getTime();
        return (int) ((m - n) / 1000);
    }

    /**
     * 计算两个日期间相隔的小时，当第一个日期小于第二个日期时，是负数
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return
     */
    public static int getHourBetween(Date date1, Date date2) {
        long m = date1.getTime();
        long n = date2.getTime();
        return (int) ((m - n) / 3600000);
    }

    /**
     * 取得两个时间之间的天数，当第一个日期小于第二个日期时，是负数
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return
     */
    public static int getDayBetween(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);
        return (int) ((c1.getTimeInMillis() - c2.getTimeInMillis()) / 86400000);
    }

    /**
     * 计算两个日期间相隔的月份数，当第一个日期小于第二个日期时，是负数
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return
     */
    public static int getMonthBetween(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        return (c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR)) * 12
                + (c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH));
    }


    /**
     * 根据周几的数字标记获得周几的汉字描述
     *
     * @param weekNum
     * @return
     */
    public static String getCnWeekDesc(int weekNum) {
        String strWeek = "";
        switch (weekNum) {
            case 1:
                strWeek = "周一";
                break;
            case 2:
                strWeek = "周二";
                break;
            case 3:
                strWeek = "周三";
                break;
            case 4:
                strWeek = "周四";
                break;
            case 5:
                strWeek = "周五";
                break;
            case 6:
                strWeek = "周六";
                break;
            case 7:
                strWeek = "周日";
                break;
        }
        return strWeek;
    }

    /**
     * 获得'上下午'标识
     *
     * @param date
     * @return
     */
    public static String getCnAMPM(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (Calendar.AM == cal.get(Calendar.AM_PM))
            return "上午";
        else
            return "下午";
    }

    /**
     * 判断是否是润年
     *
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        Calendar calendar = Calendar.getInstance();
        return ((GregorianCalendar) calendar).isLeapYear(year);
    }


    /**
     * 根据传入时间，获取时间所在周的第一天
     *
     * @param date
     * @return
     */
    public static Date getBeginDateofThisWeek(Date date) {
        Calendar current = Calendar.getInstance();
        current.setTime(date);
        int dayOfWeek = current.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == 1) { //如果是星期天，星期一则往前退6天
            current.add(Calendar.DAY_OF_MONTH, -6);
        } else {
            current.add(Calendar.DAY_OF_MONTH, 2 - dayOfWeek);
        }
        current.set(Calendar.HOUR_OF_DAY, 0);
        current.set(Calendar.MINUTE, 0);
        current.set(Calendar.SECOND, 0);
        current.set(Calendar.MILLISECOND, 0);
        return current.getTime();
    }

    /**
     * 根据传入时间，获取时间所在周的最后一天
     *
     * @param date
     * @return
     */
    public static Date getEndDateofThisWeek(Date date) {
        Calendar current = Calendar.getInstance();
        current.setTime(date);
        int dayOfWeek = current.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek != 1) { //如果不是是星期天
            current.add(Calendar.DAY_OF_MONTH, 9 - dayOfWeek);
        } else {
            current.add(Calendar.DAY_OF_MONTH, 1);
        }
        current.set(Calendar.HOUR_OF_DAY, 0);
        current.set(Calendar.MINUTE, 0);
        current.set(Calendar.SECOND, 0);
        current.set(Calendar.MILLISECOND, -1);
        return current.getTime();
    }

    /**
     * 获取从今天开始未来一周的星期和日期的映射表
     *
     * @return
     */
    public static Map<String, Date> getDateForWeekDay() {
        Map<String, Date> weekDayDateMap = new HashMap<String, Date>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 1; i <= 7; i++) {
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (dayOfWeek == 0) {
                dayOfWeek = 7;
            }
            weekDayDateMap.put(dayOfWeek + "", calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return weekDayDateMap;
    }

    /**
     * 获取日期的中国式星期几（1-7分别代表周一至周日）
     *
     * @param date
     * @return
     */
    public static int getDayOfWeek(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int today = cal.get(Calendar.DAY_OF_WEEK);
        if (today == 1)
            return 7;
        else
            return today - 1;
    }

    /**
     * 获取指定日期是一年中的第几周,从周一为起始周
     *
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setFirstDayOfWeek(Calendar.MONDAY);
            cal.setMinimalDaysInFirstWeek(4);
            cal.setTime(date);
            return cal.get(Calendar.WEEK_OF_YEAR);
        } else {
            return 0;
        }
    }

    /**
     * 获取指定日期是哪年哪周,从周一为起始周
     * 每年的第一周的最小天数为4
     *
     * @param date
     * @return yyyy年第xx周
     */
    public static String getYearAndWeek(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(4);
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        if (week == 1 && month == 12) {
            year++;
        } else if (week > 5 && month == 1) {
            year--;
        }
        StringBuilder sb = new StringBuilder().append(year).append(YEAR).append(THE);
        if (week < 10) {
            sb.append(STRING_ZERO);
        }
        sb.append(week).append(WEEK);
        return sb.toString();
    }

    /**
     * 获取年份
     *
     * @param date
     * @return
     * @Author 吴明华
     * @Date 2015年5月21日
     */
    public static int getYear(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.YEAR);
        } else {
            return 0;
        }
    }

    /**
     * 获取月份
     *
     * @param date
     * @return
     * @Author 吴明华
     * @Date 2015年5月21日
     */
    public static int getMonthOfYear(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.MONTH);
        } else {
            return 0;
        }
    }

    /**
     * 获取小时
     *
     * @param hour
     * @return
     */
    public static int getDayOfMonth(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.HOUR_OF_DAY);
        } else {
            return 0;
        }
    }

    /**
     * 获取分钟
     *
     * @param hour
     * @return
     */
    public static int getMiniteOfDay(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.MINUTE);
        } else {
            return 0;
        }
    }

    /**
     * 获取秒
     *
     * @param hour
     * @return
     */
    public static int getSecondOfDay(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.SECOND);
        } else {
            return 0;
        }
    }

    /**
     * 清除日期的时间部分
     *
     * @param date
     * @return
     * @Author 吴明华
     * @Date 2015年5月22日
     */
    public static Date clearTime(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        } else {
            return null;
        }
    }

    /**
     * 按yyyyMMdd G:i:s格式比较两个日期的大小
     *
     * @param date1 比较的第一个日期
     * @param date2 比较的第二个日期
     * @return 两个日期相等返回0, date1大于date2返回1, date1小于date2返回-1.
     */
    public static int compareTime(Date date1, Date date2) {
        long intDate1 = date1.getTime();
        long intDate2 = date2.getTime();
        return intDate1 >= intDate2 ? (intDate1 == intDate2 ? 0 : 1) : -1;
    }

    /**
     * 获取到昨天的字符串
     *
     * @return
     */
    public static String getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat(DATA_FORMAT_yyyy_MM_dd).format(cal.getTime());
        return yesterday;
    }

    /**
     * 获取到当天的24小时
     *
     * @return
     */
    public static List<Date> getDayHours() {
        List<Date> dayHours = new ArrayList<Date>();
        Date todayBegin = getTodayBegin();
        for (int i = 1; i <= 24; i++) {
            dayHours.add(getAddHourDate(todayBegin, i));
        }
        return dayHours;
    }


    /**
     * 根据传入的日期和时间，生成日期
     *
     * @param date 传入日期
     * @param time 时间，格式 HH:mm:ss 或 HH:mm
     * @return 日期
     */
    public static Date createDay(Date date, String time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (time.length() == 5) {
            time += ":00";
        }
        String[] intTime = time.split(":");
        cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(intTime[0]));
        cal.set(Calendar.MINUTE, Integer.valueOf(intTime[1]));
        cal.set(Calendar.SECOND, Integer.valueOf(intTime[2]));
        return cal.getTime();
    }

    /**
     * 计算两日之间的间隔天数
     *
     * @param startday 开始日期
     * @param endday   结束日期
     * @return 间隔天数
     */
    public static int getDays(Date startday, Date endday) {
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        startCal.setTime(startday);
        endCal.setTime(endday);
        return (int) ((endCal.getTime().getTime() - startCal.getTime()
                .getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * 根据秒数返回多少天多少小时
     *
     * @param second
     * @return
     */
    public static String calculateTimeStr(Long second) {
        StringBuffer buffer = new StringBuffer();
        long days = second / (60 * 60 * 24);
        if (0 != days) {
            buffer.append(days + "天");
        }
        long hours = (second % (60 * 60 * 24)) / (60 * 60);
        if (0 != hours) {
            buffer.append(hours + "小时");
        }
        long minutes = (second % (60 * 60)) / 60;
        if (0 != minutes) {
            buffer.append(minutes + "分钟");
        }
        long seconds = second % 60;
        if (0 != seconds) {
            buffer.append(seconds + "秒");
        }
        if (second < 1) {
            return "1秒";
        }
        return buffer.toString();
    }

    /**
     * 中国式二十四点;返回1点；2点；3点；
     *
     * @return
     */
    public static List<String> getTodayHourByChina() {
        List<String> list = new ArrayList<String>();
        for (int i = 1; i <= 24; i++) {
            list.add(i + "点");
        }
        return list;
    }

    /**
     * 中国式31号;返回1号；2号；3号；
     *
     * @return
     */
    public static List<String> getTodayDayByChina() {
        List<String> list = new ArrayList<String>();
        for (int i = 1; i <= 31; i++) {
            list.add(i + "号");
        }
        return list;
    }

    /**
     * 中国式12个月
     *
     * @return
     */
    public static List<String> getAllMonthByChina() {
        List<String> list = new ArrayList<String>();
        for (int i = 1; i <= 12; i++) {
            list.add(i + "月");
        }
        return list;
    }

    /**
     * 判断currentTime是否在startTime和endTime之间
     *
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @param currentTime 传入的时间
     * @return 在中间true；反之
     */
    public static boolean compareDate(Date startTime, Date endTime, Date currentTime) {
        Long startMin = startTime.getTime();
        Long endMin = endTime.getTime();
        Long currentMin = currentTime.getTime();
        if (startMin <= currentMin && currentMin <= endMin) {
            return true;
        }
        return false;
    }

    public static boolean compareHMSDate(String startTime, String endTime, String currentTime) {
        String yyyyMMDdd = "1970-01-01 ";
        Date startDate = DateUtil.stringToDate(yyyyMMDdd + startTime, DateUtil.DATA_FORMAT_yyyy_MM_dd_HH_mm_ss);
        Date endDate = DateUtil.stringToDate(yyyyMMDdd + endTime, DateUtil.DATA_FORMAT_yyyy_MM_dd_HH_mm_ss);
        Date currentDate = DateUtil.stringToDate(yyyyMMDdd + currentTime, DateUtil.DATA_FORMAT_yyyy_MM_dd_HH_mm_ss);
        Long startMin = startDate.getTime();
        Long endMin = endDate.getTime();
        Long currentMin = currentDate.getTime();
        if (startMin <= currentMin && currentMin <= endMin) {
            return true;
        }
        return false;
    }

    /**
     * 判断currentTime是否在startTime和endTime之间
     *
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @param currentTime 传入的时间
     * @return 在中间true；反之
     */
    public static boolean compareFeeRuleDate(Date startTime, Date endTime, Date currentTime) {
        Long startMin = startTime.getTime();
        Long endMin = endTime.getTime();
        Long currentMin = currentTime.getTime();
        if (startMin < currentMin && currentMin < endMin) {
            return true;
        }
        return false;
    }

    /**
     * 将Date转换为int形 格式(yyyyMMdd)
     *
     * @param date 日期对象
     * @return 日期的整数形式
     */
    public static int toInteger(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR) * 10000 + (c.get(Calendar.MONTH) + 1) * 100
                + c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 按yyyyMMdd格式比较两个日期的大小
     */
    public static boolean compareDateByYMD(Date startTime, Date endTime, Date currentTime) {
        int start = toInteger(startTime);
        int end = toInteger(endTime);
        int current = toInteger(currentTime);
        if (start <= current && current <= end) {
            return true;
        }
        return false;
    }

    /**
     * 按yyyyMMdd格式比较两个日期的大小
     */
    public static int compareDateByYMD(Date time1, Date time2) {
        int t1 = toInteger(time1);
        int t2 = toInteger(time2);
        if (t1 > t2) {
            return 1;
        } else if (t2 == t1) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * 计算间隔时间，m天n时i分j秒格式
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public static String getIntervalTime(Date beginTime, Date endTime) {
        long m = beginTime.getTime();
        long n = endTime.getTime();
        long interval = n - m;
        String result = "";
        if (interval >= 0) {
            interval = interval / 1000;
            int day = (int) (interval / 86400);
            int hour = 0;
            int minite = 0;
            if (day > 0) {
                interval = interval - day * 86400;
            }
            hour = (int) (interval / 3600);
            if (hour > 0) {
                interval = interval - hour * 3600;
            }
            minite = (int) (interval / 60);
            if (minite > 0) {
                interval = interval - minite * 60;
            }
            if (day > 0) {
                result = day + DAY_TI + hour + HOUR + minite + MINITE + interval + SECOND;
            } else if (hour > 0) {
                result = hour + HOUR + minite + MINITE + interval + SECOND;
            } else if (minite > 0) {
                result = minite + MINITE + interval + SECOND;
            } else {
                result = interval + SECOND;
            }
        }
        return result;
    }

    /**
     * 在出入时间基础上加上  分钟数
     *
     * @param originalDate
     * @param minute
     * @return
     */
    public static Date getPlusMinuteDate(Date originalDate, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + minute);
        return cal.getTime();
    }

    /**
     * 组合时区
     */
    public static String timeZone(Long timeOffset) {
        BigDecimal localZone = BigDecimal.valueOf((float) timeOffset / (60 * 60 * 1000));
        BigDecimal curZone = BigDecimal.valueOf(8).subtract(localZone);
        String zoneStr = curZone.abs().toString();
        String[] zones = zoneStr.split("\\.");
        if (zones[0].length() == 1) {
            zones[0] = "0" + zones[0];
        }
        if (curZone.compareTo(new BigDecimal(0)) == 1) {
            return "+" + zones[0] + ":" + ("5".equals(zones[1]) ? "30" : "00");
        } else if (curZone.compareTo(new BigDecimal(0)) == -1) {
            return "-" + zones[0] + ":" + ("5".equals(zones[1]) ? "30" : "00");
        } else if (curZone.compareTo(new BigDecimal(0)) == 0) {
            return "+00:00";
        }
        return "+16:00";
    }

    /**
     * 根据本期时间计算上期时间(Long);【日】【周】【月】【年】
     */
    public static Long getLastStamp(Long timestamp, int dimension) {
        Long stamp = new Date().getTime();
        if (dimension == 1) {
            stamp = timestamp - 24 * 3600 * 1000;
        } else if (dimension == 2) {
            stamp = timestamp - 7 * 24 * 3600 * 1000;
        } else if (dimension == 3) {
            Date date = new Date(timestamp);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, -1);
            stamp = calendar.getTimeInMillis();
        } else if (dimension == 4) {
            Date date = new Date(timestamp);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, -1);
            stamp = calendar.getTimeInMillis();
        }
        return stamp;
    }

    /**
     * 根据本期时间计算上期时间(Date);【日】【周】【月】【年】
     */
    public static Date getLastDate(Long timestamp, int dimension) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getLastStamp(timestamp, dimension));
        return calendar.getTime();
    }

    /**
     *  * 季度一年四季， 第一季度：1月-3月， 第二季度：4月-6月， 第三季度：7月-9月， 第四季度：10月-12月
     *  *
     *  * @param month
     *  *            需要查找的月份0-11,Java中的月份是从0开始计算的.
     *  * @return 当前季度开始的月份.分别是0=1月,3=4月,6=7月,9=10月
     *  
     */
    public static int getQuarterByMonth(int month) {
        int months[] = {0, 3, 6, 9};
        if (month >= 0 && month <= 2) // 1-3月;0,1,2
            return months[0];
        else if (month >= 3 && month <= 5) // 4-6月;3,4,5
            return months[1];
        else if (month >= 6 && month <= 8) // 7-9月;6,7,8
            return months[2];
        else
            // 10-12月;10,11,12
            return months[3];
    }

    /**
     * 日期解析
     *
     * @param strDate
     * @param pattern
     * @return
     */
    public static Date parseDate(String strDate) {
        return DateUtil.stringToDate(strDate, DateUtil.DATA_FORMAT_yyyy_MM_dd);
    }

    public static int getYear(String date) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(parseDate(date));
        int year = c.get(Calendar.YEAR);
        return year;
    }

    public String getYearMonth(Date date) {
        return formatDateByFormat(date, DateUtil.DATA_FORMAT_yyyy_MM);
    }

    /**
     * 取得指定月份的第一天
     *
     * @param strdate String
     * @return String
     */
    public String getMonthBegin(Date date) {
        return formatDateByFormat(date, DateUtil.DATA_FORMAT_yyyy_MM) + "-01";
    }

    /**
     * 取得指定月份的最后一天
     *
     * @param strdate String
     * @return String
     */
    public static String getMonthEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return formatDateByFormat(calendar.getTime(), DateUtil.DATA_FORMAT_yyyy_MM_dd);
    }

    /**
     * 以指定的格式来格式化日期
     *
     * @param date   Date
     * @param format String
     * @return String
     */
    public static String formatDateByFormat(Date date, String pattern) {
        String result = "";
        if (date != null) {
            result = DateUtil.dateToString(date, pattern);
        }
        return result;
    }

    /**
     * format date
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return formatDate(date, null);
    }

    /**
     * format date
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        String strDate = null;
        if (pattern == null) {
            pattern = DateUtil.DATA_FORMAT_yyyy_MM_dd;
        }
        strDate = DateUtil.dateToString(date, pattern);
        return strDate;
    }

    /**
     * 取得日期：年
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        return month + 1;
    }

    /**
     * 取得日期：年
     *
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int da = c.get(Calendar.DAY_OF_MONTH);
        return da;
    }

    /**
     * 取得当天日期是周几
     *
     * @param date
     * @return
     */
    public static int getWeekDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int week_of_year = c.get(Calendar.DAY_OF_WEEK);
        return week_of_year - 1;
    }

    /**
     * getWeekBeginAndEndDate
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String getWeekBeginAndEndDate(Date date, String pattern) {
        Date monday = getMondayOfWeek(date);
        Date sunday = getSundayOfWeek(date);
        return formatDate(monday, pattern) + " - " + formatDate(sunday, pattern);
    }

    /**
     * 根据日期取得对应周周一日期
     *
     * @param date
     * @return
     */
    public static Date getMondayOfWeek(Date date) {
        Calendar monday = Calendar.getInstance();
        monday.setTime(date);
        monday.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
        monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return monday.getTime();
    }

    /**
     * 根据日期取得对应周周日日期
     *
     * @param date
     * @return
     */
    public static Date getSundayOfWeek(Date date) {
        Calendar sunday = Calendar.getInstance();
        sunday.setTime(date);
        sunday.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
        sunday.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return sunday.getTime();
    }

    /**
     * 取得月的剩余天数
     *
     * @param date
     * @return
     */
    public static int getRemainDayOfMonth(Date date) {
        int dayOfMonth = getDayOfMonth1(date);
        int day = getPassDayOfMonth(date);
        return dayOfMonth - day;
    }

    /**
     * 取得月已经过的天数
     *
     * @param date
     * @return
     */
    public static int getPassDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 取得月天数
     *
     * @param date
     * @return
     */
    public static int getDayOfMonth1(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 取得月第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    /**
     * 取得月最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    /**
     * 取得季度第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDateOfSeason(Date date) {
        return getFirstDateOfMonth(getSeasonDate(date)[0]);
    }

    /**
     * 取得季度最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDateOfSeason(Date date) {
        return getLastDateOfMonth(getSeasonDate(date)[2]);
    }

    /**
     * 取得季度天数
     *
     * @param date
     * @return
     */
    public static int getDayOfSeason(Date date) {
        int day = 0;
        Date[] seasonDates = getSeasonDate(date);
        for (Date date2 : seasonDates) {
            day += getDayOfMonth1(date2);
        }
        return day;
    }

    /**
     * 取得季度剩余天数
     *
     * @param date
     * @return
     */
    public static int getRemainDayOfSeason(Date date) {
        return getDayOfSeason(date) - getPassDayOfSeason(date);
    }

    /**
     * 取得季度已过天数
     *
     * @param date
     * @return
     */
    public static int getPassDayOfSeason(Date date) {
        int day = 0;
        Date[] seasonDates = getSeasonDate(date);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);

        if (month == Calendar.JANUARY || month == Calendar.APRIL
                || month == Calendar.JULY || month == Calendar.OCTOBER) {// 季度第一个月
            day = getPassDayOfMonth(seasonDates[0]);
        } else if (month == Calendar.FEBRUARY || month == Calendar.MAY
                || month == Calendar.AUGUST || month == Calendar.NOVEMBER) {// 季度第二个月
            day = getDayOfMonth1(seasonDates[0])
                    + getPassDayOfMonth(seasonDates[1]);
        } else if (month == Calendar.MARCH || month == Calendar.JUNE
                || month == Calendar.SEPTEMBER || month == Calendar.DECEMBER) {// 季度第三个月
            day = getDayOfMonth1(seasonDates[0]) + getDayOfMonth1(seasonDates[1])
                    + getPassDayOfMonth(seasonDates[2]);
        }
        return day;
    }

    /**
     * 取得季度月
     *
     * @param date
     * @return
     */
    public static Date[] getSeasonDate(Date date) {
        Date[] season = new Date[3];
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int nSeason = getSeason(date);
        if (nSeason == 1) {// 第一季度
            c.set(Calendar.MONTH, Calendar.JANUARY);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.FEBRUARY);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.MARCH);
            season[2] = c.getTime();
        } else if (nSeason == 2) {// 第二季度
            c.set(Calendar.MONTH, Calendar.APRIL);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.MAY);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.JUNE);
            season[2] = c.getTime();
        } else if (nSeason == 3) {// 第三季度
            c.set(Calendar.MONTH, Calendar.JULY);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.AUGUST);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.SEPTEMBER);
            season[2] = c.getTime();
        } else if (nSeason == 4) {// 第四季度
            c.set(Calendar.MONTH, Calendar.OCTOBER);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.NOVEMBER);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.DECEMBER);
            season[2] = c.getTime();
        }
        return season;
    }

    /**
     * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
     *
     * @param date
     * @return
     */
    public static int getSeason(Date date) {
        int season = 0;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                season = 1;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                season = 2;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                season = 3;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return season;
    }

    /**
     * 判断时间是否在时间段内
     *
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @param flag
     * @return
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime, String flag) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        if (flag.equals("1")) {
            end.set(Calendar.HOUR_OF_DAY, 23);
            end.set(Calendar.SECOND, 59);
            end.set(Calendar.MINUTE, 59);
            end.set(Calendar.MILLISECOND, 999);
        }
        if (date.after(begin) && date.before(end)) {
            return true;
        } else if (nowTime.compareTo(beginTime) == 0 || nowTime.compareTo(endTime) == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static int compare_date(Date dt1, Date dt2) {
        if (dt1.getTime() > dt2.getTime()) {
            return 1;
        } else if (dt1.getTime() < dt2.getTime()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 取得指定日期所在周的最后一天
     */
    public static String getLastDayOfWeek(String date) {
        Date time = DateUtil.stringToDate(date, DateUtil.DATA_FORMAT_yyyy_MM_dd);
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(time);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        return DateUtil.dateToString(c.getTime(), DateUtil.DATA_FORMAT_yyyy_MM_dd);
    }

    /**
     * compareDate方法
     * <p>方法说明：
     * 比较endDate是否是晚于startDate；
     * 如果是，返回true， 否则返回false
     * </p>
     */
    public static boolean compareDate(String startDate, String endDate) {
        Date date1 = DateUtil.stringToDate(startDate, DateUtil.DATA_FORMAT_yyyy_MM_dd);
        Date date2 = DateUtil.stringToDate(endDate, DateUtil.DATA_FORMAT_yyyy_MM_dd);
        if (date1.getTime() > date2.getTime()) {
            return false;
        }
        return true; //startDate时间上早于endDate
    }

    /**
     * 获取一个时间范围的日期
     *
     * @param dBegin
     * @param dEnd
     * @return
     */
    public static List<Date> findDates(Date dBegin, Date dEnd) {
        List<Date> lDate = new ArrayList<Date>();
        lDate.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        return lDate;
    }

    /**
     * 取得指定日期所在周的第一天
     */
    public static String getFirstDayOfWeek(String date) {
        Date time = DateUtil.stringToDate(date, DateUtil.DATA_FORMAT_yyyy_MM_dd);
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(time);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return DateUtil.dateToString(c.getTime(), DateUtil.DATA_FORMAT_yyyy_MM_dd);
    }

    public static Date getUTCTime(Date date) {
        Calendar cal = Calendar.getInstance();
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return cal.getTime();
    }

    /**
     * 获取服务器时区偏移量
     *
     * @param date
     * @return
     */
    public static int getServerZoneOffset() {
        Calendar cal = Calendar.getInstance();
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        return zoneOffset + dstOffset;
    }

    /**
     * 通过utc时间获取服务器时间
     *
     * @param date
     * @return
     */
    public static Date getServerTime(Date utcTime) {
        Calendar cal = Calendar.getInstance();
        if (utcTime == null) {
            return cal.getTime();
        }
        cal.setTime(utcTime);
        cal.add(Calendar.MILLISECOND, getServerZoneOffset());
        return cal.getTime();
    }

    /**
     * 获取当前时间的月初时间
     *
     * @return
     */
    public static String thisMonth() {
        int x;                  // 日期属性：年
        int y;                  // 日期属性：月
        Calendar localTime = Calendar.getInstance();     // 当前日期
        String strY = null;
        x = localTime.get(Calendar.YEAR);
        y = localTime.get(Calendar.MONTH) + 1;
        strY = y >= 10 ? String.valueOf(y) : ("0" + y);
        return x + "-" + strY + "-01";
    }

    /**
     * 获取当前时间的月末时间
     *
     * @return
     */
    public static String thisMonthEnd() {
        int x;                  // 日期属性：年
        int y;                  // 日期属性：月
        String strY = null;
        String strZ = null;
        boolean leap = false;
        Calendar localTime = Calendar.getInstance();     // 当前日期
        x = localTime.get(Calendar.YEAR);
        y = localTime.get(Calendar.MONTH) + 1;
        if (y == 1 || y == 3 || y == 5 || y == 7 || y == 8 || y == 10 || y == 12) {
            strZ = "31";
        }
        if (y == 4 || y == 6 || y == 9 || y == 11) {
            strZ = "30";
        }
        if (y == 2) {
            leap = leapYear(x);
            if (leap) {
                strZ = "29";
            } else {
                strZ = "28";
            }
        }
        strY = y >= 10 ? String.valueOf(y) : ("0" + y);
        return x + "-" + strY + "-" + strZ;
    }

    public static boolean leapYear(int year) {
        boolean leap;
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                if (year % 400 == 0) leap = true;
                else leap = false;
            } else leap = true;
        } else leap = false;
        return leap;
    }

    /**
     * 功能：得到当前季度季初 格式为：xxxx-yy-zz
     *
     */
    public static String thisSeason() {
        int x;                  // 日期属性：年
        int y;                  // 日期属性：月
        Calendar localTime = Calendar.getInstance();     // 当前日期
        String dateString = "";
        x = localTime.get(Calendar.YEAR);
        y = localTime.get(Calendar.MONTH) + 1;
        if (y >= 1 && y <= 3) {
            dateString = x + "-" + "01" + "-" + "01";
        }
        if (y >= 4 && y <= 6) {
            dateString = x + "-" + "04" + "-" + "01";
        }
        if (y >= 7 && y <= 9) {
            dateString = x + "-" + "07" + "-" + "01";
        }
        if (y >= 10 && y <= 12) {
            dateString = x + "-" + "10" + "-" + "01";
        }
        return dateString;
    }

    /**
     * 功能：得到当前季度季末 格式为：xxxx-yy-zz
     */
    public static String thisSeasonEnd() {
        int x;                  // 日期属性：年
        int y;                  // 日期属性：月
        Calendar localTime = Calendar.getInstance();     // 当前日期
        String dateString = "";
        x = localTime.get(Calendar.YEAR);
        y = localTime.get(Calendar.MONTH) + 1;
        if (y >= 1 && y <= 3) {
            dateString = x + "-" + "03" + "-" + "31";
        }
        if (y >= 4 && y <= 6) {
            dateString = x + "-" + "06" + "-" + "30";
        }
        if (y >= 7 && y <= 9) {
            dateString = x + "-" + "09" + "-" + "30";
        }
        if (y >= 10 && y <= 12) {
            dateString = x + "-" + "12" + "-" + "31";
        }
        return dateString;
    }

    /**
     * 功能：得到当前年份年初 格式为：xxxx-yy-zz
     */
    public static String thisYear() {
        int x;                  // 日期属性：年
        Calendar localTime = Calendar.getInstance();     // 当前日期
        x = localTime.get(Calendar.YEAR);
        return x + "-01" + "-01";
    }

    /**
     * 功能：得到当前年份年底 格式为：xxxx-yy-zz
     */
    public static String thisYearEnd() {
        int x;                  // 日期属性：年
        Calendar localTime = Calendar.getInstance();     // 当前日期
        x = localTime.get(Calendar.YEAR);
        return x + "-12" + "-31";
    }

    /**
     * 功能：得到当前日期 格式为：xxxx-yy-zz
     */
    public static String today() {
        int x;                  // 日期属性：年
        int y;                  // 日期属性：月
        int z;                  // 日期属性：月
        Calendar localTime = Calendar.getInstance();     // 当前日期
        String strY = null;
        String strZ = null;
        x = localTime.get(Calendar.YEAR);
        y = localTime.get(Calendar.MONTH) + 1;
        z = localTime.get(Calendar.DATE);
        strY = y >= 10 ? String.valueOf(y) : ("0" + y);
        strZ = z >= 10 ? String.valueOf(z) : ("0" + z);
        return x + "-" + strY + "-" + strZ;
    }

    /**
     * 获取周末日期
     *
     * @return
     */
    public static Map<String, Date> getWeekDate() {
        Map<String, Date> map = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        //设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        //获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayWeek == 1) {
            dayWeek = 8;
        }
        //根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);
        Date mondayDate = cal.getTime();
        cal.add(Calendar.DATE, 4 + cal.getFirstDayOfWeek());
        Date sundayDate = cal.getTime();
        map.put("mondayDate", mondayDate);
        map.put("sundayDate", sundayDate);
        return map;
    }

    /**
     * 获取某一天的前一天日期 yyyy-MM-dd
     *
     * @param strData yyyy-MM-dd
     * @return
     */
    public static String getPreDateByDate(String strData) {
        Calendar c = Calendar.getInstance();
        Date date = parseDate(strData);
        c.setTime(date);
        int day1 = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day1 - 1);
        return formatDate(c.getTime(), DATA_FORMAT_yyyy_MM_dd);
    }

    /**
     * 获取某一天的前一月日期 yyyy-MM-dd
     *
     * @param strData yyyy-MM-dd
     * @return
     */
    public static String getPreMonthDateByDate(String strData) {
        Calendar c = Calendar.getInstance();
        Date date = parseDate(strData);
        c.setTime(date);
        int day1 = c.get(Calendar.MONTH);
        c.set(Calendar.MONTH, day1 - 1);
        return formatDate(c.getTime(), DATA_FORMAT_yyyy_MM_dd);
    }

    /**
     * 获取某一天的当月1号yyyy-MM-dd
     *
     * @param strData yyyy-MM-dd
     * @return
     */
    public static String getMonthOneDateByDate(String strData) {
        Calendar c = Calendar.getInstance();
        Date date = parseDate(strData);
        c.setTime(date);
        c.set(Calendar.DATE, 1);
        return formatDate(c.getTime(), DATA_FORMAT_yyyy_MM_dd);
    }

    /**
     * 获取某一天的当月最后一天yyyy-MM-dd
     *
     * @param strData yyyy-MM-dd
     * @return
     */
    public static String getMonthLastDateByDate(String strData) {
        Calendar c = Calendar.getInstance();
        Date date = parseDate(strData);
        c.setTime(date);
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return formatDate(c.getTime(), DATA_FORMAT_yyyy_MM_dd);
    }

    /**
     * 获取某一天的当年1号yyyy-MM-dd
     *
     * @param strData yyyy-MM-dd
     * @return
     */
    public static String getYearOneDateByDate(String strData) {
        Calendar c = Calendar.getInstance();
        Date date = parseDate(strData);
        c.setTime(date);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DATE, 1);
        return formatDate(c.getTime(), DATA_FORMAT_yyyy_MM_dd);
    }

    /**
     * 获取某一天的当年最后一天yyyy-MM-dd
     *
     * @param strData yyyy-MM-dd
     * @return
     */
    public static String getYearLastDateByDate(String strData) {
        Calendar c = Calendar.getInstance();
        Date date = parseDate(strData);
        c.setTime(date);
        c.set(Calendar.MONTH, 11);
        c.set(Calendar.DATE, 31);
        return formatDate(c.getTime(), DATA_FORMAT_yyyy_MM_dd);
    }

    /**
     * 获取某一天的前一年日期 yyyy-MM-dd
     *
     * @param strData yyyy-MM-dd
     * @return
     */
    public static String getPreYearDateByDate(String strData) {
        Calendar c = Calendar.getInstance();
        Date date = parseDate(strData);
        c.setTime(date);
        int day1 = c.get(Calendar.YEAR);
        c.set(Calendar.YEAR, day1 - 1);
        return formatDate(c.getTime(), DATA_FORMAT_yyyy_MM_dd);
    }

    public static String intToStringDuration(int duration) {
        String result = "";
        int hours = 0, minutes = 0, seconds = 0;
        hours = duration / 3600;
        minutes = (duration - hours * 3600) / 60;
        seconds = (duration - (hours * 3600 + minutes * 60));
        result = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return result;
    }

    public static long durationMills(Date start, Date end) {
        if (null != start && null != end) {
            throw new RuntimeException("param can not be null!");
        }
        return end.getTime() - start.getTime();
    }

    public static String durationZH(Date start, Date end) {
        if (null == start || null == end) {
            return StringUtils.EMPTY;
        }
        return mills2ZHDuration(durationMills(start, end));
    }

    public static String mills2ZHDuration(long millsDuration) {
        return second2ZHDuration(millsDuration / 1000);
    }


    public static String second2ZHDuration(long duration) {
        if (duration <= 0) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        long days = duration / 86400;
        long hours = (duration = duration - days * 86400) / 3600;
        long minutes = (duration = duration - hours * 3600) / 60;
        long seconds = duration - (minutes * 60);
        if (days > 0) {
            sb.append(days).append("天");
        }
        if (hours > 0) {
            sb.append(hours).append("小时");
        }
        if (minutes > 0) {
            sb.append(minutes).append("分钟");
        }
        if (seconds > 0) {
            sb.append(seconds).append("秒");
        }
        return sb.toString();
    }

    public static Date parseCommon(String src) {
        return stringToDate(src, DATA_FORMAT_yyyy_MM_dd_HH_mm_ss);
    }

    public static String formatCommon(Date date) {
        return formatDate(date, DATA_FORMAT_yyyy_MM_dd_HH_mm_ss);
    }

    public static String formatNowCommon() {
        return formatCommon(new Date());
    }

    public static void main(String[] args) {
        System.out.println(formatCommon(parseCommon(formatNowCommon())));
    }
}
