package com.framing.baselib;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TLog {
	public static final String LOG_TAG = "TAAS";
	public static boolean DEBUG = true;

	public TLog() {
	}
	public static final void analytics(String log) {
		if (DEBUG)
			Log.d(LOG_TAG, log);
	}

	public static final void error(String log) {
		if (DEBUG)
			Log.e(LOG_TAG, "" + log);
	}

	public static final void log(String log) {
		if (DEBUG)
			Log.i(LOG_TAG, log);
	}

	public static final void log(String tag, String log) {
		if (DEBUG) {
			//我们采取分段打印日志的方法：当长度超过4000时，我们就来分段截取打印
			//剩余的字符串长度如果大于4000
			if (log.length() > 4000) {
				for (int i = 0; i < log.length(); i += 4000) {
					//当前截取的长度<总长度则继续截取最大的长度来打印
					if (i + 4000 < log.length()) {
						Log.i(tag + i, log.substring(i, i + 4000));
					} else {
						//当前截取的长度已经超过了总长度，则打印出剩下的全部信息
						Log.i(tag + i, log.substring(i, log.length()));
					}
				}
			} else {
				//直接打印，并且数据显示json格式化后的样子
//				Log.i(tag, StringUtils.isEmpty(log) ? "" : log);
				printJson(tag, log,tag);
			}
		}
	}


	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	public static void printLine(String tag, boolean isTop) {
		if (isTop) {
			Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
		} else {
			Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
		}
	}
	public static void printJson(String tag, String msg, String headString) {

		String message;

		try {
			if (msg.startsWith("{")) {
				JSONObject jsonObject = new JSONObject(msg);
				message = jsonObject.toString(4);//最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
			} else if (msg.startsWith("[")) {
				JSONArray jsonArray = new JSONArray(msg);
				message = jsonArray.toString(4);
			} else {
				message = msg;
			}
		} catch (JSONException e) {
			message = msg;
		}

		printLine(tag, true);
		message = headString + LINE_SEPARATOR + message;
		String[] lines = message.split(LINE_SEPARATOR);
		for (String line : lines) {
			Log.d(tag, "║ " + line);
		}
		printLine(tag, false);
	}



	public static final void logv(String log) {
		if (DEBUG)
			Log.v(LOG_TAG, log);
	}

	public static final void warn(String log) {
		if (DEBUG)
			Log.w(LOG_TAG, log);
	}
}
