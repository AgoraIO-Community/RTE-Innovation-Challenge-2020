package com.zhou.appmanager.MyAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.zhou.appmanager.R;
import com.zhou.appmanager.model.AppInfo;

import java.io.File;
import java.util.List;

//自定义adapter
public class AppInfoAdapter extends BaseAdapter {
    private List<AppInfo> appInfos;
    private Context context;
    public AppInfoAdapter(List<AppInfo> list, Context context) {
        this.appInfos = list;
        this.context = context;
    }

    //计算需要适配的item总数
    @Override
    public int getCount() {
        return appInfos.size();
    }

    //获取每一个item对象
    @Override
    public Object getItem(int position) {
        return appInfos.get(position);
    }

    //获取每一个item的ID值
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //View view = View.inflate(BaseAdapterActivity.this, R.layout.item_base_adapter, null);
        Holder holder;
        //如果convertView是null，加载item的布局文件
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_base_adapter, null);
            holder = new Holder();
            //实例化对象
            holder.imageView = convertView.findViewById(R.id.appIcon);
            holder.appName = convertView.findViewById(R.id.appName);
            holder.packageName = convertView.findViewById(R.id.packageName);
            holder.appPermission = convertView.findViewById(R.id.appPermission);
            holder.apkSize = convertView.findViewById(R.id.apkSize);
            holder.allSize = convertView.findViewById(R.id.allSize);
            holder.appSize = convertView.findViewById(R.id.appSize);
            holder.dataSize = convertView.findViewById(R.id.dataSize);
            holder.cacheSize = convertView.findViewById(R.id.cacheSize);

            //打标签
            convertView.setTag(holder);
        } else {
            //进行复用
            holder= (Holder) convertView.getTag();
        }
        //赋值
        AppInfo appInfo = appInfos.get(position);
        holder.imageView.setImageDrawable(appInfo.getAppIcon());
        holder.appName.setText(appInfo.getAppName());
        holder.packageName.setText(appInfo.getPackageName());
        holder.appPermission.setText("权限:"+ appInfo.getPermissionInfos().length);
        String appSize;
        File file = new File(appInfo.getApplicationInfo().sourceDir);
        if (file.length() / 1000 > 1024) {
            appSize = String.format("%.1f", file.length() / 1048576.0)+"MB";
        } else {
            appSize=file.length()/1024+"KB";
        }
        holder.apkSize.setText("apk大小:"+appSize);
        if (appInfo.getAllSize() /1000>1000) {
            holder.allSize.setText("总计:"+String.format("%.1f",appInfo.getAllSize()/1000000.0)+"MB");
        } else {
            holder.allSize.setText("总计:"+String.format("%.1f",appInfo.getAllSize()/1000.0)+"KB");
        }
        if (appInfo.getAppSize() /1000>1000) {
            holder.appSize.setText("应用:"+String.format("%.1f",appInfo.getAppSize()/1000000.0)+"MB");
        } else {
            holder.appSize.setText("应用:"+String.format("%.1f",appInfo.getAppSize()/1000.0)+"KB");
        }
        if (appInfo.getDataSize() /1000>1000) {
            holder.dataSize.setText("数据:"+String.format("%.1f",appInfo.getDataSize()/1000000.0)+"MB");
        } else {
            holder.dataSize.setText("数据:"+String.format("%.1f",appInfo.getDataSize()/1000.0)+"KB");
        }
        if (appInfo.getCacheSize() /1000>1000) {
            holder.cacheSize.setText("缓存:"+String.format("%.1f",appInfo.getCacheSize()/1000000.0)+"MB");
        } else {
            holder.cacheSize.setText("缓存:"+String.format("%.1f",appInfo.getCacheSize()/1000.0)+"KB");
        }

        return convertView;
    }
    class Holder{
        ImageView imageView;
        TextView appName;
        TextView packageName;
        TextView appPermission;
        TextView apkSize;
        TextView allSize;
        TextView appSize;
        TextView dataSize;
        TextView cacheSize;
    }
}
