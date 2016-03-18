/**
 * Copyright(c) 2011-2012 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ijay
 */
public class DateUtils {
    // 一年12月
    public static final int MOUTHCOUNT = 12;

    /**
     * 以“今天”为基准生成day指定的日期，“日”以下数据将被置零。若“今天”的日期＝day，则直接返回“今天”
     *
     * @param day
     *        日期
     * @param afterToday
     *        是否“在今天之后”。如“今天”为2014-1-13，day为25，则afterToday为true时返回“2014-2-25”，
     *        false时返回2013-12-25
     * @return 新的日期对象
     */
    public static Date getDate4FixDay(int day, boolean afterToday) {
        return DateUtils.getDate4FixDay(new Date(), day, afterToday);
    }

    /**
     * 以date为基准生成day指定的日期，“日”以下数据将被置零。若date的日期＝day，则直接返回date
     *
     * @param date
     *        基准日期
     * @param day
     *        日期
     * @param afterToday
     *        是否应“在date之后”
     * @return 新的日期对象
     */
    public static Date getDate4FixDay(Date date, int day, boolean afterToday) {
        date = org.apache.commons.lang3.time.DateUtils.truncate(date,
            Calendar.DAY_OF_MONTH);
        Date baseDay = org.apache.commons.lang3.time.DateUtils.setDays(date,
            day);

        if (org.apache.commons.lang3.time.DateUtils.isSameDay(baseDay, date)) { // NOPMD by jay on 15-1-8 下午6:29
            // do nothing
        } else if (afterToday && baseDay.before(date)) {
            baseDay = org.apache.commons.lang3.time.DateUtils.addMonths(
                baseDay, 1);
        } else if (!afterToday && baseDay.after(date)) {
            baseDay = org.apache.commons.lang3.time.DateUtils.addMonths(
                baseDay, -1);
        }

        return baseDay;
    }

    /**
     * 计算date1（先）与date2（后）之间的差值
     *
     * @param date1
     *        起始时间
     * @param date2
     *        结束时间
     * @param field
     *        指定差值单位，由Calendar中常量字段定义
     * @return date2-date1
     */
    public static long getDelta(Date date1, Date date2, int field) {
        switch (field) {
            case Calendar.MILLISECOND:
                return DateUtils.getDeltaMilliseconds(date1, date2);
            case Calendar.SECOND:
                return DateUtils.getDeltaSeconds(date1, date2);
            case Calendar.MINUTE:
                return DateUtils.getDeltaMinutes(date1, date2);
            case Calendar.HOUR:
            case Calendar.HOUR_OF_DAY:
                return DateUtils.getDeltaHours(date1, date2);
            case Calendar.DAY_OF_MONTH:
            case Calendar.DAY_OF_WEEK:
            case Calendar.DAY_OF_WEEK_IN_MONTH:
            case Calendar.DAY_OF_YEAR:
                return DateUtils.getDeltaDays(date1, date2);
            case Calendar.WEEK_OF_MONTH:
            case Calendar.WEEK_OF_YEAR:
                return DateUtils.getDeltaWeeks(date1, date2);
            case Calendar.MONTH:
                return DateUtils.getDeltaMonths(date1, date2);
            case Calendar.YEAR:
                return DateUtils.getDeltaYears(date1, date2);
            default:
                throw new IllegalArgumentException(
                    "\"field\" should be among MILLISECOND, SECOND, MINUTE, HOUR, HOUR_OF_DAY, DAY_OF_MONTH, "
                        + "DAY_OF_WEEK, DAY_OF_WEEK_IN_MONTH, DAY_OF_YEAR, WEEK_OF_MONTH, WEEK_OF_YEAR, MONTH, YEAR");
        }

    }

    /**
     * 两个时间相差的年份
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int getDeltaYears(Date date1, Date date2) {
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        return cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
    }

    /**
     * 获取两个时间间相差的月数，date1－date2
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int getDeltaMonths(Date date1, Date date2) {
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        int monthDelta = cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH);
        int yearDalta = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);

        if (yearDalta != 0) {
            monthDelta += 12 * yearDalta;
        }

        return monthDelta;
    }

    /**
     * 获取两个时间间相差的月数，date1－date2，若date2的日期>=date1的日期，月数进一
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int getDeltaMonth(Date date1, Date date2) {
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        int monthDelta = cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH);
        int yearDalta = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);

        if (yearDalta != 0) {
            monthDelta += 12 * yearDalta;
        }

        if (cal2.get(Calendar.DATE) >= cal1.get(Calendar.DATE)) {
            monthDelta += 1;
        }

        return monthDelta;
    }

    public static int getDeltaWeeks(Date date1, Date date) {
        return DateUtils.getDeltaDays(date1, date) / 7;
    }

    public static int getDeltaDays(Date date1, Date date2) {
        return (int) (DateUtils.getDeltaHours(date1, date2) / 24);
    }

    public static long getDeltaHours(Date date1, Date date2) {
        return DateUtils.getDeltaMinutes(date1, date2) / 60;
    }

    public static long getDeltaMinutes(Date date1, Date date2) {
        return DateUtils.getDeltaSeconds(date1, date2) / 60;

    }

    public static long getDeltaSeconds(Date date1, Date date2) {
        return DateUtils.getDeltaMilliseconds(date1, date2) / 1000;
    }

    public static long getDeltaMilliseconds(Date date1, Date date2) {
        return date2.getTime() - date1.getTime();
    }

    /**
     * 日期加天数|时分秒归零
     *
     * @param date
     *        需要修改的日期
     * @param addDayNo
     *        需要添加的天数
     * @return
     */
    public static Date addDaysNTruncate(Date date, int addDayNo) {
        date = org.apache.commons.lang3.time.DateUtils.truncate(date,
            Calendar.DATE);
        return org.apache.commons.lang3.time.DateUtils.addDays(date, addDayNo);
    }

    /**
     * 按days获取离baseDay最近（小于等于）的「回款日」。e.g. days = {10,
     * 25}，baseDay为2013-4-1时返回2013-3-25，baseDay为2013-4-11时返回2013-4-10，
     * baseDay为2013-4-25时返回2013-4-25
     *
     * @param baseDay
     * @param days
     * @return
     */
    public static Date getCurrentRepayDate(Date baseDay, Integer... days) {
        List<Integer> dayList = Arrays.asList(days);
        Collections.sort(dayList);
        Collections.reverse(dayList);

        baseDay = org.apache.commons.lang3.time.DateUtils.truncate(baseDay,
            Calendar.DATE);
        Date theDay = null;

        for (int day : dayList) {
            theDay = org.apache.commons.lang3.time.DateUtils.setDays(baseDay,
                day);

            if (baseDay.compareTo(theDay) >= 0) {
                return theDay;
            }
        }

        return org.apache.commons.lang3.time.DateUtils.addMonths(
            org.apache.commons.lang3.time.DateUtils.setDays(baseDay,
                dayList.get(0)), -1);
    }

    /**
     * 根据时间获取季节
     *
     * @param date
     * @return
     * @author caoming
     */
    public static long getDateSeason(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        long month = calendar.get(Calendar.MONTH) + 1;
        if (month >= 1 && month <= 3) {
            return 1L;
        } else if (month >= 4 && month <= 6) {
            return 2L;
        } else if (month >= 7 && month <= 9) {
            return 3L;
        } else if (month >= 10 && month <= 12) {
            return 4L;
        }
        return 0L;
    }

    /**
     * 获取时间的年份
     *
     * @param date
     * @return
     * @author caoming
     */
    public static Integer getDateYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取时间的月份
     *
     * @param date
     * @return
     * @author caoming Email:caoming@ucredit.com
     */
    public static Integer getDateMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取时间的日
     *
     * @param date
     * @return
     * @author caoming Email:caoming@ucredit.com
     */
    public static Integer getDateDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取时间的年月
     *
     * @param date
     * @return
     * @author caoming
     */
    public static String getDateYear4MonthByString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return String.format("%s%s%s%s", year, "年", month, "月");
    }

    /**
     * 时间格式化成“yyyy-MM-dd”
     *
     * @param date
     * @return
     */
    public static String getFormatDateString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static DateFormat getDateFormatForLdapUTCDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
        format.setTimeZone(TimeZone.getTimeZone("utc"));

        return format;
    }

    /**
     * 判断日期格式是否合格
     *
     * @param dateString
     * @return boolean
     */
    public static boolean isValidFormat(String dateString) {
        Pattern p = Pattern.compile("\\d{4}+[-]\\d{1,2}+[-]\\d{1,2}+");
        Matcher m = p.matcher(dateString);
        return m.matches();
    }

    /**
     * 判断day天是否过期
     *
     * @param date
     *        初始时间
     * @param day
     *        天数
     * @return
     */
    public static boolean isDateBefore(Date date, int day) {
        Date endDay = org.apache.commons.lang3.time.DateUtils
            .addDays(date, day);
        return endDay.before(new Date());
    }
}
