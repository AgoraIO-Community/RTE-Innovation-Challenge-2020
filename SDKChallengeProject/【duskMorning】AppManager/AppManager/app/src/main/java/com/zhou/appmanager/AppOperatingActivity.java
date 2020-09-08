package com.zhou.appmanager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.zhou.appmanager.model.AppInfo;
import com.zhou.appmanager.util.AppUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AppOperatingActivity extends AppCompatActivity {

    private AppInfo appInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_operating);
        Intent intent = getIntent();
        appInfo = intent.getParcelableExtra("appInfo");
        setTitle(appInfo.getAppName());

        PackageManager pm = getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(appInfo.getPackageName(), PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            installed = false;
        }
        if (!installed) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    //进入应用
    public void enterApplication(View view) {
        PackageManager packageManager = getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(appInfo.getPackageName());

        if (intent != null) {
            startActivity(getPackageManager().getLaunchIntentForPackage(appInfo.getPackageName()));
        } else {
            Toast.makeText(this, "打开失败", Toast.LENGTH_SHORT).show();
        }
    }

    //卸载应用
    public void uninstallApplication(View view) {
        Uri uri = Uri.parse("package:" + appInfo.getPackageName());
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        startActivity(intent);
        finish();
    }

    //应用详情
    public void applicationDetails(View view) {
        String packageName = appInfo.getPackageName();
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", packageName, null));
        startActivity(intent);
    }

    //提取图标
    public void saveIcon(View view) {
        //检查权限
        if (ActivityCompat.checkSelfPermission(AppOperatingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(AppOperatingActivity.this, "没有存储权限", Toast.LENGTH_SHORT).show();
        } else {

            Bitmap bm = getIntent().getParcelableExtra("appIcon");
            FileOutputStream fos;
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AppManager/Icon/";
            File file = new File(path + appInfo.getAppName() + ".png");
            //判断文件夹是否存在
            if (!new File(path).exists()) {
                new File(path).mkdirs();
            }
            Log.i("path", "saveIcon: " + path);
            try {
                fos = new FileOutputStream(file, false);
                //压缩bitmap写进outputStream 参数：输出格式  输出质量  目标OutputStream
                //格式可以为jpg,png,jpg不能存储透明
                bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
                //关闭流
                Toast.makeText(this, "文件已保存至:\n" + path + appInfo.getAppName() + ".png", Toast.LENGTH_LONG).show();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                AppUtil.exceptionToast(AppOperatingActivity.this, e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                AppUtil.exceptionToast(AppOperatingActivity.this, e.getMessage());
            }

        }

    }

    //提取apk
    public void saveAPK(View view) {
        if (ActivityCompat.checkSelfPermission(AppOperatingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(AppOperatingActivity.this, "没有存储权限", Toast.LENGTH_SHORT).show();
        } else {
            final ProgressDialog dialog = ProgressDialog.show(this, "复制文件", "正在复制...");

            //长时间的工作不能在主线程做，得启动分线程执行
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final String outputPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AppManager/APK/";
                        //判断文件夹是否存在，不存在则创建
                        if (!new File(outputPath).exists()) {
                            new File(outputPath).mkdirs();
                        }

                        long startTime = System.currentTimeMillis();

                        File file = new File(appInfo.getApplicationInfo().sourceDir);
                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputPath + appInfo.getAppName() + "_" + appInfo.getVersionName() + ".apk"));
                        byte[] bytes = new byte[1024];
                        int len;
                        while ((len = bis.read(bytes)) > 0) {
                            bos.write(bytes, 0, len);
                        }
                        bos.close();
                        bis.close();

                        long endTime = System.currentTimeMillis();
                        //耗时
                        final double usedTime = (endTime - startTime) / 1000.0;
                        //速度
                        final double speed = file.length() / usedTime / 1048576.0;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {//在主线程执行
                                Toast.makeText(AppOperatingActivity.this, "文件已保存至:\n" + outputPath + appInfo.getAppName() + "_" + appInfo.getVersionName() + ".apk  \n用时:" + String.format("%.1f", usedTime) + "s  速度:" + String.format("%.1f", speed) + "MB/s", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        AppUtil.exceptionToast(AppOperatingActivity.this, e.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                        AppUtil.exceptionToast(AppOperatingActivity.this, e.getMessage());
                    }

                    //移除dialog
                    dialog.dismiss();//方法在分线程执行，但内部使用Handler实现主线程移除dialog
                }
            }).start();

        }
    }

    //分享应用
    public void shareApp(View view) {
        Toast.makeText(this, "正在生成临时文件...", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(appInfo.getApplicationInfo().sourceDir);//安装包目录文件名都是base.apk
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(getExternalCacheDir(), appInfo.getAppName() + "_" + appInfo.getVersionName() + ".apk")));
                    byte[] bytes = new byte[1024];
                    int len;
                    while ((len = bis.read(bytes)) > 0) {
                        bos.write(bytes, 0, len);
                    }
                    bos.close();
                    bis.close();

                    //StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    //StrictMode.setVmPolicy(builder.build());
                    //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    //    builder.detectFileUriExposure();
                    //}

                    Uri uri;
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(AppOperatingActivity.this, "com.zhou.appmanager.fileprovider", new File(getExternalCacheDir(), appInfo.getAppName() + "_" + appInfo.getVersionName() + ".apk"));
                    } else {
                        uri = Uri.fromFile(new File(getExternalCacheDir(), appInfo.getAppName() + "_" + appInfo.getVersionName() + ".apk"));
                    }
                    //uri= Uri.fromFile(new File(getExternalCacheDir(), appInfo.getAppName() + ".apk"));


                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    //intent.setData(uri);
                    //intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    intent.setType("application/vnd.android.package-archive");//此处可发送多种文件
                    //在Activity上下文之外启动Activity需要给Intent设置FLAG_ACTIVITY_NEW_TASK标志，不然会报异常。
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //临时访问读权限  intent的接受者将被授予 INTENT 数据uri 或者 在ClipData 上的读权限。
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(intent, "分享文件"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    AppUtil.exceptionToast(AppOperatingActivity.this, e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    AppUtil.exceptionToast(AppOperatingActivity.this, e.getMessage());
                }
            }
        }).start();

    }

    //activity销毁时清除缓存
    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteDirWihtFile(getExternalCacheDir());
    }

    //删除一个目录下所有文件包括目录本身
    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                file.delete(); // 删除所有文件}
            } else if (file.isDirectory()) {
                deleteDirWihtFile(file); // 递规的方式删除文件夹}
            }
        }
        dir.delete();// 删除目录本身
    }

    //权限信息
    public void permissionInfo(View view) {
        Intent intent = new Intent(AppOperatingActivity.this, PermissionInfoActivity.class);
        intent.putExtra("appInfo", appInfo);
        startActivity(intent);
    }

    public void gotoAPPMarket(View view) {
        String str = "market://details?id=" + appInfo.getPackageName();
        Intent localIntent = new Intent(Intent.ACTION_VIEW);
        localIntent.setData(Uri.parse(str));
        startActivity(localIntent);
    }
}
