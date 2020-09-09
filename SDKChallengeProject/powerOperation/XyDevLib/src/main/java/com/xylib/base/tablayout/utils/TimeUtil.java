package com.xylib.base.tablayout.utils;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 时间转换工具
 */
public class TimeUtil {

    private static long MessageOutTime;

    private TimeUtil() {
    }


    /**
     * 时间转化为聊天界面显示字符串
     *
     * @param timeStamp 单位为秒
     */
    public static String getTimeStr(long timeStamp) {
        if (timeStamp == 0) return "";
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp * 1000);
        Date inputDate = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(inputDate);
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return "昨天" + " " + sdf.format(inputDate);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            if (calendar.before(inputTime)) {
                SimpleDateFormat sdf = new SimpleDateFormat("M" + "月" + "d" + "日" + " HH:mm");
                return sdf.format(inputDate);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "年" + "MM" + "月" + "dd" + "日" + " HH:mm");
                return sdf.format(inputDate);
            }

        }
    }

    /**
     * 时间转化为会话界面显示字符串
     *
     * @param timeStamp 单位为秒
     */
    public static String getConversationTime(long timeStamp) {
        if (timeStamp == 0) return "";
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp * 1000);
        Date inputDate = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (checkTimeIsToday(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(inputDate);
        }
        if (checkTimeIsYesterday(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return "昨天" + " " + sdf.format(inputDate);
        }
        if (checkTimeIsThisWeek(inputTime)) {
            int i = inputTime.get(Calendar.DAY_OF_WEEK);
            return getWeekByNum(i);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(inputDate);
    }

    private static String getWeekByNum(int week) {
        switch (week) {
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            case 7:
                return "星期日";
            default:
                return "";
        }
    }

    private static boolean checkTimeIsThisWeek(Calendar inputTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -6);
        return calendar.before(inputTime);
    }

    private static boolean checkTimeIsToday(Calendar inputTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.before(inputTime);
    }

    private static boolean checkTimeIsYesterday(Calendar inputTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.before(inputTime);
    }

    public static String getFormatTime(long timeStamp) {
        if (timeStamp == 0) return "";
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp * 1000);
        Date currentTimeZone = inputTime.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "年" + "MM" + "月");
        return sdf.format(currentTimeZone);
    }

    public static String getStageFormatTime(long timeStamp) {
        if (timeStamp == 0) return "";
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp * 1000);
        Date currentTimeZone = inputTime.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "年" + "MM" + "月" + "dd" + "日");
        return sdf.format(currentTimeZone);
    }

    /**
     * @param timeStamp 时间戳  单位毫秒
     * @param template
     * @return
     */
    public static String formatTime(long timeStamp, String template) {
        if (timeStamp == 0) return "";
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp);
        Date currentTimeZone = inputTime.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(template);
        return sdf.format(currentTimeZone);
    }

    /**
     * 获取漫游消息的截止时间（当天0点往前6天）
     *
     * @return 时间戳 单位 秒
     */
    public static long getMessageOutTime() {
        if (MessageOutTime <= 0) {
            Calendar todayStart = Calendar.getInstance();
            todayStart.set(Calendar.HOUR, 0);
            todayStart.set(Calendar.MINUTE, 0);
            todayStart.set(Calendar.SECOND, 0);
            todayStart.set(Calendar.MILLISECOND, 0);
            long todayStartTime = todayStart.getTime().getTime();
            MessageOutTime = todayStartTime / 1000 - 6 * 24 * 60 * 60;
        }
        return MessageOutTime;
    }

    /**
     * 获取今天开始的时间
     *
     * @return 时间戳 单位 秒
     */
    public static long getToadyStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        long todayStartTime = todayStart.getTime().getTime();
        return todayStartTime / 1000;
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日"）
     *
     * @param time
     * @return
     */
    public static String getTimes(String time) {
        if (StringUtil.isBlank(time))
            return "";
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日"）
     *
     * @param time
     * @return
     */
    public static String getFormatTime(String time) {
        if (StringUtil.isBlank(time))
            return "";
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy/MM/dd");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    public static String getBeforeTime(long timeStamp) {
        long currentTime = System.currentTimeMillis() / 1000;
        long offTime = currentTime - timeStamp;
        if (offTime / 60 < 1) {
            return offTime + "秒前";
        }
        if (offTime / (60 * 60) < 1) {
            return offTime / 60 + "分钟前";
        }
        if (offTime / (60 * 60 * 24) < 1) {
            return offTime / (60 * 60) + "小时前";
        }
        if (offTime / (60 * 60 * 24 * 7) < 1) {
            return offTime / (60 * 60 * 24) + "天前";
        }
        if (offTime / (60 * 60 * 24 * 30) < 1) {
            return "1周前";
        }
        return "1个月前";
    }

    public static int getYearFromTimeMillis(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonthFromTimeMillis(long time, int defaultMonth) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            int month = calendar.get(Calendar.MONTH) + 1;
            if (month < 1 || month > 12) {
                month = defaultMonth;
            }
            return month;
        } catch (Exception e) {
            return defaultMonth;
        }
    }

    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static String getHouseUpdateMsgTime(long timeStamp) {
        if (timeStamp == 0) return "";
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp * 1000);
        Date inputDate = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (checkTimeIsToday(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(inputDate);
        }
        if (checkTimeIsYesterday(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return "昨天" + " " + sdf.format(inputDate);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(inputDate);
    }

    public static String getAppointmentTime(long timeStamp, int type) {
        if (timeStamp == 0) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd EE");
        String time = dateFormat.format(timeStamp);
        if (type == 0) {
            return time + " 全天";
        } else if (type == 1) {
            return time + " 上午";
        } else if (type == 2) {
            return time + " 下午";
        } else {
            return time;
        }
    }

    public static String getReserverAppointmentTime(long timeStamp) {
        if (timeStamp == 0) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(timeStamp);
    }

    public static String getVideoTime(long duration) {
        if (duration == 0) {
            return "00:00";
        }
        duration = duration / 1000;
        int minute = (int) (duration / 60);
        int second = (int) (duration % 60);
        String minuteTime = new DecimalFormat("##").format(minute);
        String secondTime = new DecimalFormat("##").format(second);
        return minuteTime + ":" + secondTime;
    }

    /**
     * 获取当前系统时间，并时间戳输出
     * @return
     */
    public static String getSysTime(){
        long time=System.currentTimeMillis()/1000;//获取系统时间的10位的时间戳
        String  str=String.valueOf(time);
        return str;

    }
    public static String formatCountDownTime(long time) {
        int day, hour, minute, second;
        day = (int) (time / (24 * 60 * 60));
        time = time - day * (24 * 60 * 60);
        hour = (int) (time / (60 * 60));
        time = time - hour * (60 * 60);
        minute = (int) (time / 60);
        second = (int) (time - minute * 60);
        return String.format("%02d天%02d小时%02d分%02d秒", day, hour, minute, second);
    }
}
