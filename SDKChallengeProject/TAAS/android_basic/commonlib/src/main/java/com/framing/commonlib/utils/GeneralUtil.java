package com.framing.commonlib.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 
 * ============================================================
 * 
 * copyright ZENG　HUI (c) 2014
 * 
 * author : HUI
 * 
 * version : 1.0
 * 
 * date created : On November 24, 2014 9:14:37
 * 
 * description : General utility class
 * 
 * revision history :
 * 
 * ============================================================
 * 
 */
@SuppressLint("SimpleDateFormat")
public class GeneralUtil {
	/**
	 * Whether gender legal
	 */
	public static boolean JudgeSexQual(String sex) {
		if ("男".equals(sex) || "女".equals(sex)) {
			return true;
		}
		return false;
	}
	
	//判断是否是个合格的区号-座机号
	public static boolean judgeTelephone(String number){
		return number.matches("^0[0-9]{3}-?[0-9]{7,8}$");
	}

	/**
	 * 判断是否是合格的手机号码
	 */
	public static boolean judgePhoneQual(String number) {
		return number
				.matches("^(\\+86-|\\+86|86-|86){0,1}1[3|4|5|7|8]{1}\\d{9}$");
	}

	/**
	 * 判断是否是合格的手机号码
	 */
	public static boolean judgePhoneQual(EditText numberEt) {
		return numberEt.getText().toString().trim().matches("^1[34568]\\d{9}$");
	}

	/**
	 * To determine whether a mailbox
	 */
	public static boolean judgeEmailQual(String email) {
		return email
				.matches("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
	}

	/**
	 * To determine whether a edit text
	 */
	public static boolean judgeEmailQual(EditText emailEt) {
		return emailEt
				.getText()
				.toString()
				.trim()
				.matches(
						"^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
	}

	/**
	 * Dip into pixels
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * Pixels converted into a dip
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * Get the device unique IMEI number
	 * 
	 * @return
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String phone_imei = manager.getDeviceId();
		if (!TextUtils.isEmpty(phone_imei)) {
			// If exists, return directly
			return phone_imei;
		} else {
			// If not, get device ID, mobile phone after the restore factory
			// Settings, the ID will be reset
			String android_id = Secure.getString(context.getContentResolver(),
					Secure.ANDROID_ID);
			if (!TextUtils.isEmpty(android_id)) {
				return android_id;
			} else {
				return "63838sdivks666ina456786pkss";
			}
		}
	}

	/**
	 * Put the string into a TXT
	 * 
	 * @param data
	 * @throws Exception
	 */
	public static void StringToTxt(String data, String savePath)
			throws Exception {
		File file = new File(savePath);
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(data);
		bw.flush();
		bw.close();
		fw.close();
	}

	/**
	 * Put the base64 place a data into the file type
	 * 
	 * @param base64Code
	 * @param savePath
	 * @throws Exception
	 */
	@SuppressLint("NewApi")
	public static void decoderBase64File(String base64Code, String savePath)
			throws Exception {
		byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
		FileOutputStream out = new FileOutputStream(savePath);
		out.write(buffer);
		out.close();
	}

	/**
	 * According to the network address to read TXT file
	 * 
	 * @param urlpath
	 *            : URL address
	 * @return
	 * @throws Exception
	 */
	public static String getString(String urlpath) throws Exception {
		URL url = new URL(urlpath);
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(6 * 1000);
			if (conn.getResponseCode() == 200) {
				InputStream inStream = conn.getInputStream();
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = -1;
				while ((len = inStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
				outStream.close();
				inStream.close();
				byte[] data = outStream.toByteArray();
				return new String(data, "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void deleteFile(File file) {
		if (!file.exists()) {
			return;
		}
		if (file.isDirectory()) {
			File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
			for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
				deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
			}
			return;
		} else {
			// 文件
			file.delete();
		}

	}

	/**
	 * Shake the animation
	 * 
	 * @param counts
	 *            How many under 1 second to shake
	 * @return
	 */
	public static Animation shakeAnimation(int counts) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
		// 设置一个循环加速器，使用传入的次数就会出现摆动的效果。
		translateAnimation.setInterpolator(new CycleInterpolator(counts));
		translateAnimation.setDuration(500);
		return translateAnimation;
	}

	/**
	 * After get the phone number of four digits
	 * 
	 * @param phone
	 * @return
	 */
	public static String getBehindFourNumber(String phone) {
		return phone.substring(phone.length() - 4, phone.length());
	}

	/**
	 * The cut out of the text to capture how many characters
	 * 
	 * @param text
	 * @return
	 */
	public static String InterceptionCharacters(String text, int number) {
		if (TextUtils.isEmpty(text)) {
			return "";
		}
		if (text.length() > number) {
			text = text.substring(0, number) + "...";
		}
		return text;
	}

	/**
	 * 根据布局文件id得到view
	 * 
	 * @param layoutId
	 *            layout id
	 * @return
	 */
	public static View getView(Context context, int layoutId, ViewGroup group) {
		return View.inflate(context, layoutId, group);
	}

	/**
	 * 得到随机文字(中文汉字)
	 * 
	 * @return
	 */
	public static char getRandomChar() {
		String str = "";
		int hightPos;
		int lowPos;
		Random random = new Random();
		hightPos = (176 + Math.abs(random.nextInt(39)));
		lowPos = (161 + Math.abs(random.nextInt(93)));

		byte[] b = new byte[2];
		b[0] = Integer.valueOf(hightPos).byteValue();
		b[1] = Integer.valueOf(lowPos).byteValue();
		try {
			str = new String(b, "GBK");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str.charAt(0);
	}

	/**
	 * 设置view不可用
	 *
	 */
	public static void setViewUnAble(View... views) {
		for (View view : views)
			view.setEnabled(false);
	}

	public static void setViewInVisible(View... views) {
		for (View view : views)
			view.setVisibility(View.INVISIBLE);
	}

	public static void setViewVisible(View... views) {
		for (View view : views)
			view.setVisibility(View.VISIBLE);
	}

	public static void setViewGone(View... views) {
		for (View view : views)
			view.setVisibility(View.GONE);
	}

	/**
	 * 设置view不可见
	 *
	 */
	public static void setViewVisibility(int visibility, View... views) {
		for (View view : views)
			view.setVisibility(visibility);
	}

	/**
	 * 设置view可用
	 *
	 */
	public static void setViewAble(View... views) {
		for (View view : views)
			view.setEnabled(true);
	}

	/**
	 * 影藏软键盘
	 * 
	 * @param activity
	 */
	public static void hideSoftInputFromWindow(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		try {
			imm.hideSoftInputFromWindow(activity.getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示软键盘
	 *
	 * @param view
	 */
	public static void showSoftInputFromWindow(View view) {
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
		view.requestFocus();
		InputMethodManager inputManager = (InputMethodManager) view
				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(view, 0);
	}

	/**
	 * 影藏软键盘
	 *
	 * @param view
	 */
	public static void hideSoftInputFromWindow(View view) {
		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
		}
	}

	/**
	 * 将临时数据添加到总列表
	 * 
	 * @param <T>
	 */
	public static <T> ArrayList<T> addTempListData(ArrayList<T> allList,
                                                   ArrayList<T> tempList) {
		allList.addAll(tempList);
		return allList;
	}

	/**
	 * 检测返回数据列表
	 * 
	 * @param resultList
	 * @return
	 */
	public static boolean checkResultList(ArrayList<?> resultList) {
		return resultList != null && resultList.size() > 0;
	}

	/**
	 * 检测密码的强度
	 * 
	 * @param password
	 * @return
	 */
	public static int checkPwdStrength(String password) {
		int score = 0;
		if (password.length() <= 6) {
			score = 1;
		} else if (password.length() >= 7 && password.length() <= 10) {
			score = 2;
		} else {
			score = 3;
		}
		return score;
	}

	/**
	 * 检验某个服务是否活着
	 * 
	 * @param context
	 *            服务的包名
	 * @return
	 */
	public static boolean isServiceRunning(Context context, String serviceName) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> infos = am.getRunningServices(100);
		for (RunningServiceInfo info : infos) {
			String name = info.service.getClassName();
			if (serviceName.equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 得到HashMap中的值
	 * 
	 * @param hashMap
	 * @return
	 */
	public static ArrayList<String> getValuesByHashMap(
			HashMap<String, String> hashMap) {
		ArrayList<String> values = new ArrayList<String>();
		Iterator<Entry<String, String>> iter = hashMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			values.add(entry.getValue());
		}
		return values;
	}

	/**
	 * 创建文件夹
	 * 
	 * @param file
	 * @return
	 */
	public static boolean createFile(File file) {
		if (!file.exists()) {
			return file.mkdir();
		}
		return false;
	}

	/**
	 * 把arrayList里面重复的元素去掉
	 */
	public static <T> ArrayList<T> trimArray(ArrayList<T> arrayList) {
		ArrayList<T> resultList = new ArrayList<T>();
		for (T object : arrayList) {
			if (!resultList.contains(object)) {
				resultList.add(object);
			}
		}
		return resultList;
	}

	// 1、正则表达式
	public static boolean isNumeric(String str) {
		if (TextUtils.isEmpty(str))
			return false;

		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 通知父布局，占用的宽，高
	 * 
	 * @param view
	 */
	public static void measureView(View view) {
		ViewGroup.LayoutParams p = view.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int height;
		int tempHeight = p.height;
		if (tempHeight > 0) {
			height = MeasureSpec.makeMeasureSpec(tempHeight,
					MeasureSpec.EXACTLY);
		} else {
			height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		view.measure(width, height);
	}

	public static String IDCardValidate(String IDStr){
		String errorInfo = "";// 记录错误信息
		String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4",
				"3", "2" };
		String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
				"9", "10", "5", "8", "4", "2" };
		String Ai = "";
		// ================ 号码的长度 15位或18位 ================
		if (IDStr.length() != 15 && IDStr.length() != 18) {
			errorInfo = "身份证号码长度应该为15位或18位。";
			return errorInfo;
		}
		// =======================(end)========================

		// ================ 数字 除最后以为都为数字 ================
		if (IDStr.length() == 18) {
			Ai = IDStr.substring(0, 17);
		} else if (IDStr.length() == 15) {
			Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
		}
		if (isNumeric(Ai) == false) {
			errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
			return errorInfo;
		}
		// =======================(end)========================

		// ================ 出生年月是否有效 ================
		String strYear = Ai.substring(6, 10);// 年份
		String strMonth = Ai.substring(10, 12);// 月份
		String strDay = Ai.substring(12, 14);// 月份
		if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
			errorInfo = "身份证生日无效。";
			return errorInfo;
		}
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
					|| (gc.getTime().getTime() - s.parse(
							strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
				errorInfo = "身份证生日不在有效范围。";
				return errorInfo;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			errorInfo = "身份证月份无效";
			return errorInfo;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
			errorInfo = "身份证日期无效";
			return errorInfo;
		}
		// =====================(end)=====================

		// ================ 地区码时候有效 ================
		Hashtable<String, String> h = GetAreaCode();
		if (h.get(Ai.substring(0, 2)) == null) {
			errorInfo = "身份证地区编码错误。";
			return errorInfo;
		}
		// ==============================================

		// ================ 判断最后一位的值 ================
		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++) {
			TotalmulAiWi = TotalmulAiWi
					+ Integer.parseInt(String.valueOf(Ai.charAt(i)))
					* Integer.parseInt(Wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		String strVerifyCode = ValCodeArr[modValue];
		Ai = Ai + strVerifyCode;

		if (IDStr.length() == 18) {
			if (Ai.equals(IDStr) == false) {
				errorInfo = "身份证无效，不是合法的身份证号码";
				return errorInfo;
			}
		} else {
			return "";
		}
		// =====================(end)=====================
		return "";
	}

	/**
	 * 功能：设置地区编码
	 * 
	 * @return HashTable 对象
	 */
	private static Hashtable<String, String> GetAreaCode() {
		Hashtable<String, String> hashtable = new Hashtable<String, String>();
		hashtable.put("11", "北京");
		hashtable.put("12", "天津");
		hashtable.put("13", "河北");
		hashtable.put("14", "山西");
		hashtable.put("15", "内蒙古");
		hashtable.put("21", "辽宁");
		hashtable.put("22", "吉林");
		hashtable.put("23", "黑龙江");
		hashtable.put("31", "上海");
		hashtable.put("32", "江苏");
		hashtable.put("33", "浙江");
		hashtable.put("34", "安徽");
		hashtable.put("35", "福建");
		hashtable.put("36", "江西");
		hashtable.put("37", "山东");
		hashtable.put("41", "河南");
		hashtable.put("42", "湖北");
		hashtable.put("43", "湖南");
		hashtable.put("44", "广东");
		hashtable.put("45", "广西");
		hashtable.put("46", "海南");
		hashtable.put("50", "重庆");
		hashtable.put("51", "四川");
		hashtable.put("52", "贵州");
		hashtable.put("53", "云南");
		hashtable.put("54", "西藏");
		hashtable.put("61", "陕西");
		hashtable.put("62", "甘肃");
		hashtable.put("63", "青海");
		hashtable.put("64", "宁夏");
		hashtable.put("65", "新疆");
		hashtable.put("71", "台湾");
		hashtable.put("81", "香港");
		hashtable.put("82", "澳门");
		hashtable.put("91", "国外");
		return hashtable;
	}

	/**
	 * 获取一段字符串中的数字
	 * 
	 * @param originalStr
	 * @return
	 */
	public static final String getStringNum(String originalStr) {
		String numberStr = "";
		originalStr.trim();
		if (originalStr != null && !"".equals(originalStr)) {
			for (int i = 0; i < originalStr.length(); i++) {
				if (originalStr.charAt(i) >= 48 && originalStr.charAt(i) <= 57) {
					numberStr += originalStr.charAt(i);
				}
			}
		}

		return numberStr;
	}

	/**
	 * 功能：判断字符串是否为日期格式
	 *
	 * @return
	 */
	public static boolean isDate(String strDate) {
		Pattern pattern = Pattern
				.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
		Matcher m = pattern.matcher(strDate);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 对给定的字符串返回唯一的标记字符串<br>
	 * 主要应用于网络url的标记，将url转换映射成唯一的标识字符串<br>
	 * 写法参考Volley中的写法<br>
	 * 
	 * @param str
	 *            需要转换的字符串
	 * @return 返回唯一的标识
	 */
	public static String toHash(String str) {
		String ret = null;
		if (str != null && str.length() > 0) {
			int len = str.length();
			String part1 = str.substring(0, len / 2).hashCode() + "";
			String part2 = str.substring(len / 2).hashCode() + "";
			ret = part1 + part2;
		}
		return ret;
	}

	/**
	 * 对数据（字节）进行Base64编码
	 * 
	 * @param data
	 *            要编码的数据（字节数组）
	 * @return 返回编码后的字符串
	 */
	public static String Base64Encode(byte[] data) {
		String ret = null;
		if (data != null && data.length > 0) {
			ret = Base64.encodeToString(data, Base64.NO_WRAP);
		}
		return ret;
	}

	/**
	 * 对Base64编码后的数据进行还原
	 * 
	 * @param data
	 *            使用Base64编码过的数据
	 * @return 返回还原后的数据（字节数组）
	 */
	public static byte[] Base64Decode(String data) {
		byte[] ret = null;
		if (data != null && data.length() > 0) {
			ret = Base64.decode(data, Base64.NO_WRAP);
		}
		return ret;
	}

	/**
	 * 使用MD5获取数据的摘要信息
	 * 
	 * @param data
	 *            数据
	 * @return 摘要信息
	 */
	public static String toMD5(byte[] data) {
		String ret = null;
		try {
			byte[] digest = MessageDigest.getInstance("md5").digest(data);
			ret = Base64Encode(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return ret;
	}







	/**
	 * Input filed the cursor to the end
	 * 
	 * @param editText
	 *            input box
	 */
	public static void cursorToEnd(EditText editText) {
		Editable text = editText.getText();
		Spannable spanText = text;
		Selection.setSelection(spanText, text.length());
	}

	/**
	 * 判断是不是一个合法的url
	 */
	public static boolean checkUrl(String url) {
		if(TextUtils.isEmpty(url)) return true;
		
		return url.matches("^((https|http|ftp|rtsp|mms)?://)"
				+ "+(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?"
				+ "(([0-9]{1,3}\\.){3}[0-9]{1,3}" + "|"
				+ "([0-9a-z_!~*'()-]+\\.)*"
				+ "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." + "[a-z]{2,6})"
				+ "(:[0-9]{1,4})?" + "((/?)|"
				+ "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$");
	}
}
