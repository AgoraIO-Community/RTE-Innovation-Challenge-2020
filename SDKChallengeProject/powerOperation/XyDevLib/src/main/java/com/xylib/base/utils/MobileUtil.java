package com.xylib.base.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class MobileUtil {
    private static final String TAG = "MobileUtil";
    private static String imei = null;

    private static String mac = null;

    private static String packageName = null;

    private static int versionCode = 0;

    private static String versionName = null;

    public static final int DATASIZE = 20 * 1024 * 1024;

    private static String sdCardPath = null;

    private static int SCREENWIDTH = 0;

    private static int SCREENHEIGHT = 0;

    private static float DENSITY = 0;

    private static int DENSITYDPI = 0;

    private static String sChannel;

    private static String sIMSI = null;
    private static String firstImsi = null;
    private static String secondImsi = null;
    private static AlertDialog dialog;


    public static int getVersionCode(Context context) {
        if (versionCode == 0) {
            setVersionInfo(context);
        }
        return versionCode;
    }

    public static String getVersionName(Context context) {
        if (versionName == null) {
            setVersionInfo(context);
        }
        return versionName;
    }

    public static String getPackageName(Context context) {
        if (packageName == null) {
            setVersionInfo(context);
        }
        return packageName;
    }

    public static void setVersionInfo(Context context) {
        packageName = context.getApplicationContext().getPackageName();
        try {
            PackageInfo pm = context.getApplicationContext().getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_CONFIGURATIONS);
            versionCode = pm.versionCode;
            versionName = pm.versionName;
        } catch (NameNotFoundException e) {
        }
    }

    public static String getVersionCodeString(Context context) {
        int code = getVersionCode(context);
        StringBuilder sb = new StringBuilder();
        while (code != 0) {
            sb.append(code % 10);
            code /= 10;
            if (code != 0) {
                sb.append(".");
            }
        }
        return sb.reverse().toString();
    }

    public static int getScreenWidth(Context context) {
        setScreenSize(context);
        return SCREENWIDTH;
    }

    public static int getScreenHeight(Context context) {
        setScreenSize(context);
        return SCREENHEIGHT;
    }


    /**
     * by andrew
     */
    public static void setScreenSize(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowsManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowsManager.getDefaultDisplay().getMetrics(metrics);
        int screenwidth = metrics.widthPixels;
        int screenheight = metrics.heightPixels;
        DENSITY = metrics.density;
        DENSITYDPI = metrics.densityDpi;
        if (context.getResources()
                .getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && screenwidth >= screenheight) {
            SCREENWIDTH = Math.max(screenwidth, screenheight);
            SCREENHEIGHT = Math.min(screenwidth, screenheight);
        } else {
            SCREENWIDTH = Math.min(screenwidth, screenheight);
            SCREENHEIGHT = Math.max(screenwidth, screenheight);
        }
    }

    /**
     * 获取手机屏幕密度
     *
     * @author andrewlcgu
     */
    public static float getScreenDensity(Context context) {
        if (0 == DENSITY) {
            setScreenSize(context);
        }
        return DENSITY;
    }

    /**
     * 获取手机屏幕密度比
     *
     * @author andrewlcgu
     */
    public static int getScreenDensityDpi(Context context) {
        if (0 == DENSITYDPI) {
            setScreenSize(context);
        }
        return DENSITYDPI;
    }

    /**
     * 获得当前手机density类型
     *
     * @author andrewlcgu
     */
    public static String getDPIType(Context context) {
        int densityDPI = getScreenDensityDpi(context);
        if (densityDPI <= 120) {
            return "ldpi";
        } else if (densityDPI <= 160) {
            return "mdpi";
        } else if (densityDPI <= 240) {
            return "hdpi";
        } else if (densityDPI <= 320) {
            return "xhdpi";
        } else if (densityDPI <= 480) {
            return "xxhdpi";
        } else if (densityDPI <= 640) {
            return "xxxhdpi";
        } else {
            return "unknown";
        }
    }

    public static DisplayMetrics getDeviceDisplayMetrics(Context context) {
        WindowManager windowsManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        android.view.Display display = windowsManager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        return outMetrics;
    }

    /**
     * 获取手机状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            statusBarHeight = 38;
        }
        return statusBarHeight;
    }

    public static int dpToPx(Context context, int dp) {
        return (int) (dp * MobileUtil.getDeviceDisplayMetrics(context).density + 0.5f);
    }

    public static int dpToPx(Context context, float dp) {
        return (int) (dp * MobileUtil.getDeviceDisplayMetrics(context).density + 0.5f);
    }

    public static int pxToDp(Context context, int px) {
        return (int) (px / MobileUtil.getDeviceDisplayMetrics(context).density + 0.5f);
    }

    public static int spToPx(Context context, int sp) {
        return (int) (sp * MobileUtil.getDeviceDisplayMetrics(context).scaledDensity + 0.5f);
    }

    public static int pxToSp(Context context, int px) {
        return (int) (px / MobileUtil.getDeviceDisplayMetrics(context).scaledDensity + 0.5f);
    }

    /**
     * 获取SD卡路径，以'/'结束
     *
     * @return
     */
    public static String getSdCardPath() {
        if ((sdCardPath == null) || (sdCardPath.equals(""))) {
            sdCardPath = Environment.getExternalStorageDirectory().getPath();
            if (!sdCardPath.substring(sdCardPath.length() - 1).equals(File.separator)) {
                sdCardPath += File.separator;
            }
        }
        return sdCardPath;
    }

    public static boolean isSDCardExists() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断SD剩余容量是否大于给定值(20MB)
     *
     * @author andrewlcgu
     */
    @SuppressLint("NewApi")
    public static boolean isSDCardFull() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String sdcard = Environment.getExternalStorageDirectory().getPath();
            StatFs statFs = new StatFs(sdcard);
            long blockSize = statFs.getBlockSizeLong();
            long blocks = statFs.getAvailableBlocksLong();
            long availableSpare = blocks * blockSize;
            // 换算成MB
            return DATASIZE > availableSpare;
        }
        return false;
    }

    /**
     * 获得生产厂商
     *
     * @return
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获得操作系统
     *
     * @return
     */
    public static String getOS() {
        return "Android";
    }

    /**
     * 获得设备型号
     *
     * @return
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机android操作系统版本
     *
     * @return
     */
    public static int getSystemSdk() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机系统版本号
     *
     * @author andrewlcgu
     */
    public static String getSystemVeriosn() {
        return Build.VERSION.RELEASE;
    }


    public static boolean isTaskInScreenFront(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am.getRunningTasks(1) != null && am.getRunningTasks(1).get(0) != null) {
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            if (cn.getPackageName().equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean getDebugMode(Context context) {
        boolean ISDEBUG = false;
        PackageManager pm = context.getApplicationContext().getPackageManager();
        try {
            ApplicationInfo info = pm
                    .getApplicationInfo(context.getApplicationContext().getPackageName(), 0);
            ISDEBUG = (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            return ISDEBUG;
        } catch (NameNotFoundException e) {
            ISDEBUG = false;
            return ISDEBUG;
        }
    }

    public static boolean isLowMemory() {
        return Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() < 2 * 1024 * 1024
                && Runtime.getRuntime().freeMemory() < 2 * 1024 * 1024;
    }

    @SuppressLint("NewApi")
    public static long getInstallTime(Context context) {
        long appInstallTime = 0;
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(getPackageName(context), 0);
            final int version = Build.VERSION.SDK_INT;
            if (version >= 9) {
                appInstallTime = packageInfo.lastUpdateTime;
            } else {
                ApplicationInfo appInfo = context.getPackageManager()
                        .getApplicationInfo(getPackageName(context), 0);
                String sAppFile = appInfo.sourceDir;
                appInstallTime = new File(sAppFile).lastModified();
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return appInstallTime;
    }

    public static long getAppBuildTime(Context context) {
        ZipFile zf = null;
        boolean isError = false;
        long appBuildTime = 0;
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), 0);
            zf = new ZipFile(ai.sourceDir);
            ZipEntry ze = zf.getEntry("classes.dex");
            if (ze != null) {
                appBuildTime = ze.getTime();
            }
        } catch (Throwable t) {
            isError = true;
        } finally {
            if (zf != null) {
                try {
                    zf.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        if (isError) {
            return 1;
        }
        return appBuildTime;
    }

    //是否支持状态栏文字黑色
    public static boolean isSupportLightBar() {
        return isMIUIV6OrAbove() || isFlymeV4OrAbove() || isAndroidMOrAbove();
    }

    //Android Api 23以上
    public static boolean isAndroidMOrAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 获取魅族版本是否是Flyme v4或者以上（非魅族手机返回false）
     *
     * @return
     */
    public static boolean isFlymeV4OrAbove() {
        //Flyme V4的displayId格式为 [Flyme OS 4.x.x.xA]
        //Flyme V5的displayId格式为 [Flyme 5.x.x.x beta]
        String displayId = Build.DISPLAY;
        if (!TextUtils.isEmpty(displayId) && displayId.contains("Flyme")) {
            String[] displayIdArray = displayId.split(" ");
            for (String temp : displayIdArray) {
                //版本号4以上，形如4.x.
                if (temp.matches("^[4-9]\\.(\\d+\\.)+\\S*")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取MIUI版本是否是6或者以上（非小米手机返回false）
     *
     * @return
     */
    public static boolean isMIUIV6OrAbove() {
        //MIUI V6对应的versionCode是4
        //MIUI V7对应的versionCode是5
        String miuiVersionCodeStr = getSystemProperty("ro.miui.ui.version.code");
        if (!TextUtils.isEmpty(miuiVersionCodeStr)) {
            try {
                int miuiVersionCode = Integer.parseInt(miuiVersionCodeStr);
                if (miuiVersionCode >= 4) {
                    return true;
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

    private static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return line;
    }

    public static void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Nullable
    private static String getApkPath(@NonNull final Context context) {
        String apkPath = null;
        try {
            final ApplicationInfo applicationInfo = context.getApplicationInfo();
            if (applicationInfo == null) {
                return null;
            }
            apkPath = applicationInfo.sourceDir;
        } catch (Throwable e) {
        }
        return apkPath;
    }

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断App是否处于前台
     *
     * @param context 上下文
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isAppForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        if (infos == null || infos.size() == 0) return false;
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return info.processName.equals(context.getPackageName());
            }
        }
        return false;
    }



    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            Log.v("yxy", "sHA1==>" + result.substring(0, result.length() - 1));

            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     *  根据Uri获取文件真实地址
     */
    public static String getRealFilePath(Context context, Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String realPath = null;
        if (scheme == null)
            realPath = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            realPath = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA},
                    null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        realPath = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        if (TextUtils.isEmpty(realPath)) {
            if (uri != null) {
                String uriString = uri.toString();
                int index = uriString.lastIndexOf("/");
                String imageName = uriString.substring(index);
                File storageDir;

                storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                File file = new File(storageDir, imageName);
                if (file.exists()) {
                    realPath = file.getAbsolutePath();
                } else {
                    storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File file1 = new File(storageDir, imageName);
                    realPath = file1.getAbsolutePath();
                }
            }
        }
        return realPath;
    }
}
