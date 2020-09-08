package com.zhou.appmanager.util;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.usage.StorageStats;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.zhou.appmanager.model.AppInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class AppUtil {
    public final static int USER_APP = 1;
    public final static int SYSTEM_APP = 2;

    //获取已安装的app信息
    public static List<AppInfo> getAppInfo(int tag, Context context) {
        PackageManager pm = context.getPackageManager();
        //获取所有的app信息
        List<PackageInfo> packages = pm.getInstalledPackages(0);

        List<AppInfo> userApp = new ArrayList<>();
        List<AppInfo> systemApp = new ArrayList<>();

        for (PackageInfo packageInfo : packages) {
            // 判断系统/非系统应用
            AppInfo appInfo = new AppInfo();
            try {
                appInfo.setAppName(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
                appInfo.setAppNamePinyin(PinyinTool.getPinyinString(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString()));
                appInfo.setPackageName(packageInfo.packageName);
                appInfo.setVersionName(packageInfo.versionName);
                appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(context.getPackageManager()));
                appInfo.setApplicationInfo(packageInfo.applicationInfo);
                String[] permissions = pm.getPackageInfo(appInfo.getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions;
                if (permissions == null) {
                    permissions = new String[]{};
                }
                appInfo.setPermissionInfos(permissions);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {// 非系统应用
                userApp.add(appInfo);
            } else {  // 系统应用
                systemApp.add(appInfo);
            }
        }

        if (tag == SYSTEM_APP) {
            return systemApp;
        } else if (tag == USER_APP) {
            return userApp;
        } else {
            return null;
        }
    }

    //获取大小,需要有访问使用记录的权限
    public static void getSize(Context context, List<AppInfo> appInfos) {
        try {
            //仅在8.0及以上才执行
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                StorageStatsManager storageStatsManager = (StorageStatsManager) context.getSystemService(Context.STORAGE_STATS_SERVICE);
                StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
                List<StorageVolume> storageVolumes = storageManager.getStorageVolumes();

                for (int i = 0; i < appInfos.size(); i++) {
                    AppInfo appInfo = appInfos.get(i);
                    for (StorageVolume storageVolume : storageVolumes) {
                        String uuidStr = storageVolume.getUuid();
                        String description = storageVolume.getDescription(context);
                        //Log.e("description:", "description:"+description);
                        if (description.equals("内部存储")) {
                            UUID uuid = uuidStr == null ? StorageManager.UUID_DEFAULT : UUID.fromString(uuidStr);
                            int uid = AppUtil.getAppUid(context, appInfo.getPackageName());
                            StorageStats storageStats = storageStatsManager.queryStatsForUid(uuid, uid);
                            //总大小=应用大小+数据大小
                            long allSize = storageStats.getAppBytes() + storageStats.getDataBytes();
                            appInfo.setAllSize(allSize);
                            appInfo.setAppSize(storageStats.getAppBytes());//应用大小
                            appInfo.setDataSize(storageStats.getDataBytes());//数据大小
                            appInfo.setCacheSize(storageStats.getCacheBytes());//缓存大小
                        }
                    }
                    appInfos.set(i, appInfo);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取应用的uid
    public static int getAppUid(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            return info.uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //检测是否拥有访问使用记录的权限
    public static boolean hasUsageStatsPermission(Context context) {
        //http://stackoverflow.com/a/42390614/878126
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        final int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.getPackageName());
        boolean granted;
        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (context.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }
        return granted;
    }

    //抛出异常时打吐司
    public static void exceptionToast(final Activity activity, final String message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    //申请权限
    public static void requestPermissions(Activity activity) {
        //动态申请权限  WRITE_EXTERNAL_STORAGE
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1);
            }
        }

        //动态申请权限  READ_EXTERNAL_STORAGE
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,}, 1);
            }
        }
    }

    //根据名称排序
    public static void sortByName(int sortByName, List<AppInfo> userAppInfos, List<AppInfo> systemAppInfos) {
        if (sortByName % 2 == 0) { //变成升序排序(sortByName默认=1)
            Collections.sort(userAppInfos, new Comparator<AppInfo>() {
                @Override
                public int compare(AppInfo o1, AppInfo o2) {
                    Comparator<Object> com = java.text.Collator.getInstance(java.util.Locale.CHINA);
                    return com.compare(o1.getAppName(), o2.getAppName());
                    //Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
                    //return com.compare(o1.getAppName(), o2.getAppName());
                    //return o1.getAppName().compareTo(o2.getAppName());
                }
            });
            Collections.sort(systemAppInfos, new Comparator<AppInfo>() {
                @Override
                public int compare(AppInfo o1, AppInfo o2) {
                    Comparator<Object> com = java.text.Collator.getInstance(java.util.Locale.CHINA);
                    return com.compare(o1.getAppName(), o2.getAppName());
                    //Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
                    //return com.compare(o1.getAppName(), o2.getAppName());
                    //return o1.getAppName().compareTo(o2.getAppName());
                }
            });
        } else { //变成降序排序
            Collections.sort(userAppInfos, new Comparator<AppInfo>() {
                @Override
                public int compare(AppInfo o1, AppInfo o2) {
                    Comparator<Object> com = java.text.Collator.getInstance(java.util.Locale.CHINA);
                    return com.compare(o2.getAppName(), o1.getAppName());
                    //Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
                    //return com.compare(o2.getAppName(), o1.getAppName());
                    //return o2.getAppName().compareTo(o1.getAppName());
                }
            });
            Collections.sort(systemAppInfos, new Comparator<AppInfo>() {
                @Override
                public int compare(AppInfo o1, AppInfo o2) {
                    Comparator<Object> com = java.text.Collator.getInstance(java.util.Locale.CHINA);
                    return com.compare(o2.getAppName(), o1.getAppName());
                    //Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
                    //return com.compare(o2.getAppName(), o1.getAppName());
                    //return o2.getAppName().compareTo(o1.getAppName());
                }
            });
        }
    }

    //根据权限数量排序
    public static void sortByPermissions(int sortByPermissions, List<AppInfo> userAppInfos, List<AppInfo> systemAppInfos) {
        if (sortByPermissions % 2 == 0) {
            Collections.sort(userAppInfos, new Comparator<AppInfo>() {
                @Override
                public int compare(AppInfo o1, AppInfo o2) {
                    int i = o1.getPermissionInfos().length - o2.getPermissionInfos().length;
                    //if (i > 0) {
                    //    return 1;
                    //} else if (i == 0) {
                    //    return 0;
                    //} else {
                    //    return -1;
                    //}
                    //return (i < 0) ? -1 : ((i == 0) ? 0 : 1);
                    return Integer.compare(i, 0);
                }
            });
            Collections.sort(systemAppInfos, new Comparator<AppInfo>() {
                @Override
                public int compare(AppInfo o1, AppInfo o2) {
                    int i = o1.getPermissionInfos().length - o2.getPermissionInfos().length;
                    return Integer.compare(i, 0);
                }
            });
        } else {
            Collections.sort(userAppInfos, new Comparator<AppInfo>() {
                @Override
                public int compare(AppInfo o1, AppInfo o2) {
                    int i = o1.getPermissionInfos().length - o2.getPermissionInfos().length;
                    if (i > 0) {
                        return -1;
                    } else if (i == 0) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
            Collections.sort(systemAppInfos, new Comparator<AppInfo>() {
                @Override
                public int compare(AppInfo o1, AppInfo o2) {
                    int i = o1.getPermissionInfos().length - o2.getPermissionInfos().length;
                    return Integer.compare(0, i);
                }
            });
        }
    }

    //根据apk大小排序
    public static void sortByAPKSize(int sortBySize, List<AppInfo> userAppInfos, List<AppInfo> systemAppInfos) {
        if (sortBySize % 2 == 0) {
            Collections.sort(userAppInfos, new Comparator<AppInfo>() {
                @Override
                public int compare(AppInfo o1, AppInfo o2) {
                    int i = (int) new File(o1.getApplicationInfo().sourceDir).length() - (int) new File(o2.getApplicationInfo().sourceDir).length();
                    return Integer.compare(i, 0);
                }
            });
            Collections.sort(systemAppInfos, new Comparator<AppInfo>() {
                @Override
                public int compare(AppInfo o1, AppInfo o2) {
                    int i = (int) new File(o1.getApplicationInfo().sourceDir).length() - (int) new File(o2.getApplicationInfo().sourceDir).length();
                    return Integer.compare(i, 0);
                }
            });
        } else {
            Collections.sort(userAppInfos, new Comparator<AppInfo>() {
                @Override
                public int compare(AppInfo o1, AppInfo o2) {
                    int i = (int) new File(o1.getApplicationInfo().sourceDir).length() - (int) new File(o2.getApplicationInfo().sourceDir).length();
                    return Integer.compare(0, i);
                }
            });
            Collections.sort(systemAppInfos, new Comparator<AppInfo>() {
                @Override
                public int compare(AppInfo o1, AppInfo o2) {
                    int i = (int) new File(o1.getApplicationInfo().sourceDir).length() - (int) new File(o2.getApplicationInfo().sourceDir).length();
                    return Integer.compare(0, i);
                }
            });
        }
    }

    //根据apk大小排序
    public static void sortByALLSize(int sortByALLSize, List<AppInfo> userAppInfos, List<AppInfo> systemAppInfos) {
        if (sortByALLSize % 2 == 0) {
            Collections.sort(userAppInfos, new Comparator<AppInfo>() {
                @Override
                public int compare(AppInfo o1, AppInfo o2) {
                    int i = (int) o1.getAllSize() - (int) o2.getAllSize();
                    return Integer.compare(i, 0);
                }
            });
            Collections.sort(systemAppInfos, new Comparator<AppInfo>() {
                @Override
                public int compare(AppInfo o1, AppInfo o2) {
                    int i = (int) o1.getAllSize() - (int) o2.getAllSize();
                    return Integer.compare(i, 0);
                }
            });
        } else {
            Collections.sort(userAppInfos, new Comparator<AppInfo>() {
                @Override
                public int compare(AppInfo o1, AppInfo o2) {
                    int i = (int) o1.getAllSize() - (int) o2.getAllSize();
                    return Integer.compare(0, i);
                }
            });
            Collections.sort(systemAppInfos, new Comparator<AppInfo>() {
                @Override
                public int compare(AppInfo o1, AppInfo o2) {
                    int i = (int) o1.getAllSize() - (int) o2.getAllSize();
                    return Integer.compare(0, i);
                }
            });
        }
    }
}
