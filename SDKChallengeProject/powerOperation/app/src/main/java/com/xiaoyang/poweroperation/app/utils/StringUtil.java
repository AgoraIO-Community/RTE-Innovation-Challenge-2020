package com.xiaoyang.poweroperation.app.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtil {

    public final static String EMPTY_STRING = "";

    /**
     * 有问题,请使用
     *
     * @see StringUtil#toHexString1(byte[], String)
     */
    public static String toHexString(byte[] bytes, String separator) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(Integer.toHexString(0xFF & b)).append(separator);
        }
        return hexString.toString();
    }

    /**
     * MD5加密 32位小写MD5
     *
     * @param str
     * @return 32位小写MD5 String
     * @author sparkyou
     * @date 2015年4月8日
     */
    public static String parseStrToMd5L32(String str) {
        String reStr = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(str.getBytes());
            byte[] bytes = md5.digest();
            reStr = toHexString1(bytes, "");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return reStr;
    }

    public static String toHexString1(byte[] bytes, String separator) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bytes) {
            int bt = b & 0xff;
            if (bt < 16) {
                stringBuffer.append(0);
            }
            stringBuffer.append(Integer.toHexString(bt));
        }
        return stringBuffer.toString();
    }

    /**
     * 每个byte转字符串
     *
     * @param bytes
     * @return String
     * @author sparkyou
     * @date 2015年4月9日
     */
    public static String byteToString(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bytes) {
            stringBuffer.append((char) b);
        }
        return stringBuffer.toString();
    }

    /**
     * 根据key从形如key1=value1&key2=value2的字符串中得到value
     *
     * @param data
     * @param key
     * @return
     */
    public static String getOAuthInfo(String data, String key) {
        if (key == null) {
            return null;
        }
        if (data != null) {
            String subDatas[] = data.split("&");
            for (int i = 0; i < subDatas.length; i++) {
                String sunDatas[] = subDatas[i].split("=");
                if (key.equals(sunDatas[0])) {
                    if (sunDatas.length < 2) {
                        return "";
                    } else {
                        return sunDatas[1];
                    }
                }
            }
        }
        return null;

    }

    public static byte[] appendBytes(byte[] src, byte[] append, int start, int length) {
        if (src == null) {
            src = new byte[0];
        }
        if (append == null || length == 0) {
            return src;
        }
        int totalLength = src.length + length;
        byte[] returnBytes = new byte[totalLength];
        for (int i = 0; i < totalLength; i++) {
            if (i < src.length) {
                returnBytes[i] = src[i];
            } else {
                returnBytes[i] = append[i - src.length];
            }
        }
        return returnBytes;
    }


    /**
     * 把cal用指定的pattern来格式化
     *
     * @param cal
     * @param pattern
     * @return
     */
    public static String formatDate(Calendar cal, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(cal.getTime());
    }

    public static String getImageFileName(String url) {
        String fileName = "";
        if (null != url && url.length() > 7) {
            fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
        }
        return fileName;
    }

    public static String urlEncode(String str) throws UnsupportedEncodingException {
        if (str == null) {
            str = "";
        }
        return URLEncoder.encode(str, "utf-8").replaceAll("\\+", "%20").replaceAll("%7E", "~").replaceAll("\\*",
                "%2A");
    }

    public static String getIdFromTag(String tag) {
        if (tag == null || "".equals(tag)) {
            return "";
        }
        int indexOfDot = tag.lastIndexOf(".");
        if (indexOfDot == -1) {
            return "";
        }
        return tag.substring(indexOfDot + 1, tag.length());
    }


    public static String[] list2Array(List<String> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        int size = list.size();
        String[] strings = new String[size];
        for (int i = 0; i < size; i++) {
            strings[i] = list.get(i);
        }
        return strings;
    }


    public static String StringFilter(String str) {
        Matcher m = null;
        try {
            str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!");// 替换中文标号
            String regEx = "[『』]"; // 清除掉特殊字符
            Pattern p = Pattern.compile(regEx);
            m = p.matcher(str);
        } catch (PatternSyntaxException e) {
            return str;
        }
        return m.replaceAll("").trim();
    }


    // 判断一个字符是汉字、数字、字母还是其他（如符号）
    public static int getTypeOfChar(char a) {
        if (StringUtil.isCnHz(a)) { // 汉字
            return 0;
        } else if (Character.isDigit(a)) {
            return 1;
        } else if (Character.isLetter(a)) {
            return 2;
        } else if (StringUtil.isChinese(a) && !StringUtil.isCnHz(a)) {// 汉字符号
            return 4;
        } else {
            return 3;
        }
    }

    // 判断是否是汉字
    public static boolean isCnHz(char a) {
        // return String.valueOf(a).matches("[\u4E00-\u9FA5]"); //
        // 利用正则表达式，经测试可以区分开中文符号
        return String.valueOf(a).matches("[\u4E00-\u9FBF]");
    }

    public static String getNextWord(String str, int charType) {
        String retStr = "";
        char[] c = str.toCharArray();
        int typeOfChar = 100;
        for (int i = 0; i < c.length; i++) {
            typeOfChar = StringUtil.getTypeOfChar(c[i]);
            if (typeOfChar != charType) {
                break;
            }
            retStr += String.valueOf(c[i]);
        }
        return retStr;
    }

    // 去除空格回车换行
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 截取指定长度中文字符串（一个中文长度为2个英文字符长度）
     *
     * @param str
     * @param subLength
     * @return
     */
    public static String subString(String str, int subLength) {
        int n = 0;
        int i = 0;
        int j = 0;
        int byteNum = subLength * 2;
        boolean flag = true;
        if (str == null) {
            return "";
        }

        for (i = 0; i < str.length(); i++) {
            if ((int) (str.charAt(i)) < 128) {
                n += 1;
            } else {
                n += 2;
            }
            if (n > byteNum && flag) {
                j = i;
                flag = false;
            }
            if (n >= byteNum + 2) {
                break;
            }
        }

        if (n >= byteNum + 2 && i != str.length() - 1) {
            str = str.substring(0, j);
            str += "...";
        }
        return str;
    }

    private static DecimalFormat df = new DecimalFormat("#,###");

    public static String priceConversion(String price) {
        if (TextUtils.isEmpty(price)) {
            return price;
        }
        price = price.replace(".00", "");
        price = price.replace(".0", "");
        try {
            return df.format(Double.valueOf(price));
        } catch (Exception e) {
            return price;
        }
    }

    /**
     * 得到输入框中输入的字符长度
     */
    public static int getLength(String strName) {
        char[] ch = strName.toCharArray();
        int length = 0;
        for (int i = 0; i < ch.length; i++) {
            // 这是以前谁写的?想干啥都不清楚,只能先这么改了... by andrew
            // char c = ch[i];
            // if (isChinese(c) == true) {
            // length += 2;
            // } else {
            // length += 2;
            // }
            length += 2;
        }
        if (length % 2 != 0) {
            length = length / 2 + 1;
        } else {
            length = length / 2;
        }
        return length;
    }

    /**
     * 判断输入的字符是否是汉字
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }

    /**
     * 生成显示文件大小的文字
     *
     * @param value 字节大小
     * @return
     */
    public static String calcFileSize(double value) {
        DecimalFormat formater = new DecimalFormat("#0.##");
        double sizeK = value / 1024;
        double sizeM = sizeK / 1024;
        if (sizeK < 1000) {
            return Math.round(sizeK) + "K";
        }
        return formater.format(sizeM) + "M";
    }

    /**
     * 格式double型数据
     *
     * @param db
     * @return
     */
    public static String formatDouble(double db) {
        if (db == 0) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#,###.00");
        String priceString = df.format(db);
        if (priceString.contains(".")) {

            String endStr = priceString.substring(priceString.indexOf("."), priceString.length());
            char[] chars = endStr.toCharArray();
            int endIndex = (priceString.length() - 1);
            for (int i = (chars.length - 1); i >= 0; i--) {
                if (chars[i] == '0') {
                    endIndex = endIndex - 1;
                } else {
                    break;
                }
            }

            if (endIndex == priceString.indexOf(".")) {
                priceString = priceString.substring(0, endIndex);
            } else {
                priceString = priceString.substring(0, endIndex + 1);
            }
        }
        return priceString;
    }

    /**
     * 验证手机号码
     *
     * @param phone
     * @return
     */
    public static boolean isPhoneNumber(String phone) {
        if (TextUtils.isEmpty(phone))
            return false;
        return phone.matches("^0{0,1}(13[0-9]|14[0-9]|15[0-9]|18[0-9])[0-9]{8}");
    }

    public static String formatPercentage(double db) {
        return String.format("%.2f", db);
    }

    /**
     * 返回不是null的字符串
     */
    public static String getNonNullString(String str) {
        if (str == null) {
            str = "--";
        }
        return str;
    }

    /**
     * 返回不是null的字符串
     */
    public static String getNonNullString(String str, String def) {
        if (str == null || str.equals("")) {
            str = def;
        }
        return str;
    }

    public static boolean startWithIgnoreCase(String str, String prefix) {
        if (str == null || prefix == null) {
            return false;
        }
        str = str.toLowerCase(Locale.US);
        prefix = prefix.toLowerCase(Locale.US);
        return str.startsWith(prefix);
    }

    /**
     * java实现不区分大小写高亮替换
     *
     * @param source
     * @param searchText 需要高亮显示的字符
     * @return
     */
    public static String IgnoreCaseReplace(String source, String searchText) {
        if (StringUtil.isBlank(searchText)) {
            return source;
        }
        Pattern p = Pattern.compile(Pattern.quote(searchText), Pattern.CASE_INSENSITIVE);
        Matcher mc = p.matcher(source);
        StringBuffer sb = new StringBuffer();
        while (mc.find()) {
            mc.appendReplacement(sb, "<font color='#019b79'>" + mc.group()
                    + "</font>");
        }
        mc.appendTail(sb);
        return sb.toString();
    }

    /**
     * 正则表达式判断是否为合法Url链接
     *
     * @param url
     * @return boolean
     * @author v_zhuizhao
     * @date 2015年3月5日
     */
    public static boolean isLegalURL(String url) {
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern patt = Pattern.compile(regex);
        Matcher matcher = patt.matcher(url);
        return matcher.matches();
    }


    public static HashMap<String, String> arrayToMap(String[] arr) {

        HashMap<String, String> map = new HashMap<String, String>();

        for (int i = 0; i < arr.length; i++) {

            String[] strRow = arr[i].split(",");

            if (strRow.length <= 1) continue;

            map.put(strRow[0], strRow[1]);
        }


        return map;
    }

    public static List<Map<String, String>> arrayToList(String[] arr) {

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        for (int i = 0; i < arr.length; i++) {

            String[] strRow = arr[i].split(",");

            if (strRow.length <= 1) continue;

            Map<String, String> map = new HashMap<String, String>();
            map.put("key", strRow[0]);
            map.put("name", strRow[1]);

            //当前的值
            list.add(map);

        }


        return list;
    }

    /**
     * 逗号分割的字符串拆分为数组
     *
     * @param str
     * @return
     */
    public static String[] convertStrToArray(String str) {
        String[] strArray = null;
        strArray = str.split(","); //拆分字符为"," ,然后把结果交给数组strArray
        return strArray;
    }

    /**
     * 逗号分割为list
     *
     * @param str
     * @return
     */
    public static List<String> convertStrToList(String str) {
        List<String> strList = new ArrayList<>();
        String[] strArray = null;
        strArray = str.split(","); //拆分字符为"," ,然后把结果交给数组strArray
        return Arrays.asList(strArray);
    }

    /**
     * 采用默认格式的时间
     *
     * @param time
     * @return
     */
    public static String getPublishTime(long time) {

        String yearPattern = "yyyy-MM-dd HH:MM";
        String twoDaysAgoPtn = "MM-dd HH:MM";
        String twoDaysPtn = "前天HH:MM";
        String yesterdayPtn = "昨天HH:MM";
        String todayPtn = "HH:MM";

        //调整为毫秒级别的
        time *= 1000;

        Calendar currentCal = Calendar.getInstance();
        Calendar publicCal = Calendar.getInstance();
        publicCal.setTimeInMillis(time);

        long dif = (currentCal.getTimeInMillis() - time) / 1000;

        if (dif < 60) {
            //1分钟内
            return "刚刚";
        } else if (dif < 3600) {
            //1小时内
            return Math.round(dif / 60) + "分钟前";
        } else {

            //是否年份一致
            int currentYear = currentCal.get(Calendar.YEAR);
            int publicYear = publicCal.get(Calendar.YEAR);

            // 如果年份非自然年，则显示yyyy-MM-dd
            if (publicYear < currentYear) {
                return formatDate(publicCal, yearPattern);
            }

            //看天数
            int currentDay = currentCal.get(Calendar.DAY_OF_YEAR);
            int publicDay = publicCal.get(Calendar.DAY_OF_YEAR);
            // 如果两天前的则显示为MM-dd HH:mm
            if (currentDay - publicDay >= 3) {
                return formatDate(publicCal, twoDaysAgoPtn);
            }
            // 前天
            if (currentDay - publicDay == 2) {
                return formatDate(publicCal, twoDaysPtn);
            }
            // 昨天
            if (currentDay - publicDay == 1) {
                return formatDate(publicCal, yesterdayPtn);
            }

            // 当天
            return formatDate(publicCal, todayPtn);
        }
    }

    /**
     * time to string，秒的值
     *
     * @param time
     * @param format
     * @return
     */
    public static String timeToString(long time, String format) {

        if (time <= 0) return "";

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(time * 1000);

        return formatDate(calendar, format);
    }

    /**
     * 日期字符串转换成时间戳类型的
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static long dateToTimestamp(String dateStr, String format) {
        if (TextUtils.isEmpty(dateStr)) {
            return 0;
        }

        if (TextUtils.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm";
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);

            return (sdf.parse(dateStr).getTime() / 1000);
        } catch (Exception e) {
//			return 0;
        }

        return 0;
    }

    /**
     * 检查字符串是否为<code>null</code>或空字符串<code>""</code>。
     * <pre>
     * StringUtil.isEmpty(null)      = true
     * StringUtil.isEmpty("")        = true
     * StringUtil.isEmpty(" ")       = false
     * StringUtil.isEmpty("bob")     = false
     * StringUtil.isEmpty("  bob  ") = false
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果为空, 则返回<code>true</code>
     */
    public static boolean isEmpty(String str) {
        return ((str == null) || (str.length() == 0));
    }

    /**
     * 检查字符串是否不是<code>null</code>和空字符串<code>""</code>。
     * <pre>
     * StringUtil.isEmpty(null)      = false
     * StringUtil.isEmpty("")        = false
     * StringUtil.isEmpty(" ")       = true
     * StringUtil.isEmpty("bob")     = true
     * StringUtil.isEmpty("  bob  ") = true
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果不为空, 则返回<code>true</code>
     */
    public static boolean isNotEmpty(String str) {
        return ((str != null) && (str.length() > 0));
    }

    /**
     * 检查字符串是否是空白：<code>null</code>、空字符串<code>""</code>或只有空白字符。
     * <pre>
     * StringUtil.isBlank(null)      = true
     * StringUtil.isBlank("")        = true
     * StringUtil.isBlank(" ")       = true
     * StringUtil.isBlank("bob")     = false
     * StringUtil.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果为空白, 则返回<code>true</code>
     */
    public static boolean isBlank(String str) {
        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 检查字符串是否不是空白：<code>null</code>、空字符串<code>""</code>或只有空白字符。
     * <pre>
     * StringUtil.isBlank(null)      = false
     * StringUtil.isBlank("")        = false
     * StringUtil.isBlank(" ")       = false
     * StringUtil.isBlank("bob")     = true
     * StringUtil.isBlank("  bob  ") = true
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果为空白, 则返回<code>true</code>
     */
    public static boolean isNotBlank(String str) {
        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * 得到UUID
     *
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "").toUpperCase();
    }

    /**
     * 从字符串中获取数字串
     *
     * @param content
     * @return
     */
    public static List<String> getNumbers(String content) {
        List<String> digitList = new ArrayList<String>();
        Pattern p = Pattern.compile("(\\d+)");
        Matcher m = p.matcher(content);
        while (m.find()) {
            String find = m.group(1).toString();
            //QQ 数字长度为5的时候才会以链接的形式实现，否则按正常文本展示
            if (find != null && find.length() > 5) {
                digitList.add(find);
            }

        }
        return digitList;
    }


    public static int getFirstInt(String source) {
        if (StringUtil.isBlank(source))
            return 0;
        Pattern pattern = Pattern.compile("\\d+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(source);
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group());
            } catch (Exception ex) {
                ex.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    public static String[] hanziArray = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};

    public static String getHanziByNum(int num) {
        if (num > 12 || num < 0) {
            return "";
        }
        return hanziArray[num];
    }

    public static String getHanziByNum(String num) {
        int index;
        try {
            index = Integer.parseInt(num);
        } catch (Exception e) {
            return "";
        }
        if (index >= 10 || index < 0) {
            return "";
        }
        return hanziArray[index];
    }

    /**
     * md5
     *
     * @param string
     * @return
     */
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /***
     * 去除String数组中的空值
     */
    public static String[] deleteArrayNull(String string[]) {
        String strArr[] = string;

        // step1: 定义一个list列表，并循环赋值
        ArrayList<String> strList = new ArrayList<String>();
        for (int i = 0; i < strArr.length; i++) {
            strList.add(strArr[i]);
        }

        // step2: 删除list列表中所有的空值
        while (strList.remove(null)) ;
        while (strList.remove("")) ;

        // step3: 把list列表转换给一个新定义的中间数组，并赋值给它
        String strArrLast[] = strList.toArray(new String[strList.size()]);

        return strArrLast;
    }

    /**
     * list 去重
     *
     * @param list
     */
    public static void removeDuplicate(List<String> list) {
        LinkedHashSet<String> set = new LinkedHashSet<String>(list.size());
        set.addAll(list);
        list.clear();
        list.addAll(set);
    }

    /**
     * 处理价格单位标签
     *
     * @param price
     * @return
     */
    public static String getPriceUnit(String price) {
        if (StringUtil.isNotBlank(price)) {
            return "¥" + price;
        }
        return "";
    }

    /**
     * 处理距离
     *
     * @param distance
     * @return
     */
    public static String getDistanceUnit(float distance) {
        String distanceLabel;
        if (distance % 1000 > 1) {
            distanceLabel = distance % 1000 + "km";
        } else {
            distanceLabel = distance + "m";
        }
        return distanceLabel;
    }

    /**
     * 处理设备等级
     *
     * @param rank
     * @return
     */
    public static String getDeviceRank(int rank) {
        String rankLabel = "";
        switch (rank) {
            case 1:
                rankLabel = "A类";
                break;
            case 2:
                rankLabel = "B类";
                break;
            case 3:
                rankLabel = "C类";
                break;
            default:
                rankLabel = "未知";
                break;
        }
        return rankLabel;
    }

    /**
     * 处理计划类型
     *
     * @param type
     * @return
     */
    public static String getPlanType(int type) {
        String rankLabel = "";
        switch (type) {
            case 1:
                rankLabel = "点检";
                break;
            case 2:
                rankLabel = "巡检";
                break;
            case 3:
                rankLabel = "保养";
                break;
            case 4:
                rankLabel = "自由巡检";
                break;
            case 5:
                rankLabel = "定期保养";
                break;
        }
        return rankLabel;
    }

    /**
     * 检查范围值（闭区间）
     *
     * @param lsValue
     * @param lsMin
     * @param lsMax
     * @return
     */
    public static boolean checkValue(String lsValue, String lsMin, String lsMax) {
        boolean result = false;
        if (StringUtil.isNotBlank(lsValue) && StringUtil.isNotBlank(lsMin) && StringUtil.isNotBlank(lsMax)) {
            float value = Float.valueOf(lsValue);
            float min = Float.valueOf(lsMin);
            float max = Float.valueOf(lsMax);
            Log.v("yxy", "value" + value + "==" + min + "==" + max);
            if (value >= min && value <= max) {
                result = true;
            }
        }
        return result;

    }
}
