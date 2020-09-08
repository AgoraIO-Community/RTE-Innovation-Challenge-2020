package com.zhou.appmanager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zhou.appmanager.model.AppInfo;
import com.zhou.appmanager.util.AppUtil;

public class PermissionInfoActivity extends AppCompatActivity {

    private ListView permissionInfo_lv;
    private String[] permissionInfos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_info);
        permissionInfo_lv = findViewById(R.id.permissionInfo_lv);

        AppInfo appInfo = getIntent().getParcelableExtra("appInfo");
        permissionInfos = appInfo.getPermissionInfos();

        setTitle(appInfo.getAppName()+"的权限");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_permissioninfo, permissionInfos);
        permissionInfo_lv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.permission_info_option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.copy_all:
                StringBuilder sb = new StringBuilder();
                for (int i=0;i<permissionInfos.length;i++) {
                    if (i != permissionInfos.length - 1) {
                        sb.append(permissionInfos[i]).append("\n");
                    } else {
                        sb.append(permissionInfos[i]);
                    }
                }
                //获取剪贴板管理器
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("permissions", sb.toString());
                // 将ClipData内容放到系统剪贴板里。
                if (cm != null) {
                    Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show();
                    cm.setPrimaryClip(mClipData);
                } else {
                    NullPointerException exception = new NullPointerException();
                    AppUtil.exceptionToast(PermissionInfoActivity.this,exception.getMessage());
                    throw exception;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
