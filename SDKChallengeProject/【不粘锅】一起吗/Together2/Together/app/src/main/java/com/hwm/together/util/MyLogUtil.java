package com.hwm.together.util;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyLogUtil {

    /**
     * 日志文件总开
     *  true 为开，false 为关
     */
    public static Boolean MYLOG_SWITCH = true; // 日志文件总开启

    /**
     * 日志写入文件
     * true 为开，false 为关
     */
    public static Boolean MYLOG_WRITE_TO_FILE = true;// 日志写入文件�??�??
    private static char MYLOG_TYPE = 'v';// 输入日志类型，w代表只输出告警信息等，v代表输出�??有信�??
    private static String MYLOG_PATH_SDCARD_DIR = "manyou";// 日志文件在sdcard中的路径
    private static int SDCARD_LOG_FILE_SAVE_DAYS = 0;// sd卡中日志文件的最多保存天�??
    private static String MYLOGFILEName = "manyou_log.txt";// 本类输出的日志文件名�??
    private static SimpleDateFormat myLogSdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");// 日志的输出格�??
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式

    public static void w(String tag, Object msg) { // 警告信息
        log(tag, msg.toString(), 'w');
    }

    public static void e(String tag, Object msg) { // 错误信息
        log(tag, msg.toString(), 'e');
    }

    public static void d(String tag, Object msg) {// 调试信息
        log(tag, msg.toString(), 'd');
    }

    public static void i(String tag, Object msg) {//
        log(tag, msg.toString(), 'i');
    }

    public static void v(String tag, Object msg) {
        log(tag, msg.toString(), 'v');
    }

    public static void w(String tag, String text) {
        log(tag, text, 'w');
    }

    public static void e(String tag, String text) {
        log(tag, text, 'e');
    }

    public static void d(String tag, String text) {
        log(tag, text, 'd');
    }

    public static void i(String tag, String text) {
        log(tag, text, 'i');
    }

    public static void v(String tag, String text) {
        log(tag, text, 'v');
    }

    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param tag
     * @param msg
     * @param level
     * @return void
     * @since v 1.0
     */
    private static void log(String tag, String msg, char level) {
        if (MYLOG_SWITCH) {
            if ('e' == level && ('e' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) { // 输出错误信息
                Log.e(tag, msg);
            } else if ('w' == level && ('w' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
                Log.w(tag, msg);
            } else if ('d' == level && ('d' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
                Log.d(tag, msg);
            } else if ('i' == level && ('d' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
                Log.i(tag, msg);
            } else {
                Log.v(tag, msg);
            }
            if (MYLOG_WRITE_TO_FILE)
                writeLogtoFile(String.valueOf(level), tag, msg);
        }
    }

    private static String getLogPath() {
        String path = "";
        // 获取扩展SD卡设备状�??
        String sDStateString = android.os.Environment.getExternalStorageState();

        // 拥有可读可写权限
        if (sDStateString.equals(android.os.Environment.MEDIA_MOUNTED)) {
            // 获取扩展存储设备的文件目�??
            File SDFile = android.os.Environment.getExternalStorageDirectory();
            path = SDFile.getAbsolutePath() + File.separator
                    + MYLOG_PATH_SDCARD_DIR;
        }
        return path;

    }

    /**
     * 打开日志文件并写入日�??
     *
     * @return
     * **/
    private static void writeLogtoFile(String mylogtype, String tag, String text) {// 新建或打�??日志文件
        Date nowtime = new Date();
        String needWriteFiel = logfile.format(nowtime);
        String needWriteMessage = myLogSdf.format(nowtime) + "    " + mylogtype
                + "    " + tag + "    " + text;

        // 取得日志存放目录
        String path = getLogPath();
        if (path != null && !"".equals(path)) {
            try {
                // 创建目录
                File dir = new File(path);
                if (!dir.exists())
                    dir.mkdir();
                // 打开文件
                File file = new File(path + File.separator + needWriteFiel
                        + MYLOGFILEName);
                FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
                BufferedWriter bufWriter = new BufferedWriter(filerWriter);
                bufWriter.write(needWriteMessage);
                bufWriter.newLine();
                bufWriter.close();
                filerWriter.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除制定的日志文
     * */
    public static void delFile() {// 删除日志文件
        // 取得日志存放目录
        String path = getLogPath();
        if (path != null && !"".equals(path)) {
            String needDelFiel = logfile.format(getDateBefore());
            File file = new File(path, needDelFiel + MYLOGFILEName);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 得到现在时间前的几天日期，用来得到需要删除的日志文件�??
     * */
    private static Date getDateBefore() {
        Date nowtime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowtime);
        now.set(Calendar.DATE, now.get(Calendar.DATE)
                - SDCARD_LOG_FILE_SAVE_DAYS);
        return now.getTime();
    }
}
