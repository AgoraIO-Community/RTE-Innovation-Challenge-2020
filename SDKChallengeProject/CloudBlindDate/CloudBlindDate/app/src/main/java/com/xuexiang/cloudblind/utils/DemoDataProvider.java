/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.cloudblind.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.xuexiang.cloudblind.R;
import com.xuexiang.cloudblind.adapter.entity.NewInfo;
import com.xuexiang.cloudblind.core.entity.BannerInfo;
import com.xuexiang.xaop.annotation.MemoryCache;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 演示数据
 *
 * @author xuexiang
 * @since 2018/11/23 下午5:52
 */
public class DemoDataProvider {

    public static String[] titles = new String[]{
            "世纪佳缘 &闲鱼直播】云相亲大会",
            "【常规节目】江苏卫视《非诚勿扰》嘉宾火热招募",
            "【精品定制】大型企业联谊会",
            "爱在活力城",
            "520 上海豫园 感恩·敢爱 交友活动",

    };

    public static String[] urls = new String[]{
            "https://hd.jyimg.com/w4/party/hd_pic/1583476073864.jpeg",
            "https://hd.jyimg.com/w4/party/hd_pic/1596072211376.jpeg",
            "http://hd.jyimg.com/w4/party/hd_pic/1559185122223.jpeg",
            "https://hd.jyimg.com/w4/party/hd_pic/1596184044999.jpeg",
            "https://hd.jyimg.com/w4/party/hd_pic/1588235347544.jpeg",
    };

    public static String[] linkUrl = new String[]{
            "https://party.jiayuan.com/party_oldinfo.php?pid=9535",
            "https://party.jiayuan.com/party_info.php?pid=5745",
            "https://party.jiayuan.com/party_info.php?pid=9503",
            "https://party.jiayuan.com/party_oldinfo.php?pid=9541",
            "https://party.jiayuan.com/party_oldinfo.php?pid=9537",
    };

    @MemoryCache
    public static List<BannerItem> getBannerList() {
        List<BannerItem> list = new ArrayList<>();
        for (int i = 0; i < urls.length; i++) {
            BannerInfo item = new BannerInfo();
            item.imgUrl = urls[i];
            item.title = titles[i];
            item.linkUrl = linkUrl[i];
            list.add(item);
        }
        return list;
    }

    /**
     * 用于占位的空信息
     *
     * @return
     */
    @MemoryCache
    public static List<NewInfo> getDemoNewInfos() {
        List<NewInfo> list = new ArrayList<>();
        list.add(new NewInfo("相亲", "世纪佳缘 &闲鱼直播】云相亲大会")
                .setSummary("世纪佳缘遇见爱，线下活动等你来。这一天又有会员成功牵手了，幸福就是一步的距离，勇敢伸出你的手，或许她（他）会给你一只手！")
                .setDetailUrl("https://party.jiayuan.com/party_oldinfo.php?pid=9535")
                .setImageUrl("https://hd.jyimg.com/w4/party/hd_pic/1583476073864.jpeg"));

        list.add(new NewInfo("综艺", "江苏卫视《非诚勿扰》嘉宾火热招募")
                .setSummary("《非诚勿扰》从舞台到音乐焕然一新，原来的“扇形”舞台变为了“哑铃”式分区，将24位女嘉宾分为心动区和观察区。男嘉宾暂不亮相，在新开辟的“第二现场”与女嘉宾们“隔空对话”。")
                .setDetailUrl("https://party.jiayuan.com/party_info.php?pid=5745")
                .setImageUrl("https://hd.jyimg.com/w4/party/hd_pic/1596072211376.jpeg"));

        list.add(new NewInfo("线下活动", "大型企业联谊会")
                .setSummary("活动参与人数以现场实际到达人数为准。嘉宾必须对自己的一切行为负责，请确信您有足够的智慧和自我保护能力，免受不法侵害。")
                .setDetailUrl("https://party.jiayuan.com/party_info.php?pid=9503")
                .setImageUrl("http://hd.jyimg.com/w4/party/hd_pic/1559185122223.jpeg"));

        list.add(new NewInfo("线下活动", "爱在活力城")
                .setSummary("世纪佳缘遇见爱，线下活动等你来。这一天又有会员成功牵手了，幸福就是一步的距离，勇敢伸出你的手，或许她（他）会给你一只手！")
                .setDetailUrl("https://party.jiayuan.com/party_oldinfo.php?pid=9541")
                .setImageUrl("https://hd.jyimg.com/w4/party/hd_pic/1596184044999.jpeg"));

        list.add(new NewInfo("线下活动", "520 上海豫园 感恩·敢爱 交友活动")
                .setSummary("世纪佳缘遇见爱，线下活动等你来。这一天又有会员成功牵手了，幸福就是一步的距离，勇敢伸出你的手，或许她（他）会给你一只手！")
                .setDetailUrl("https://party.jiayuan.com/party_oldinfo.php?pid=9537")
                .setImageUrl("https://hd.jyimg.com/w4/party/hd_pic/1588235347544.jpeg"));
        return list;
    }

    public static List<AdapterItem> getGridItems(Context context) {
        return getGridItems(context, R.array.grid_titles_entry, R.array.grid_icons_entry);
    }


    private static List<AdapterItem> getGridItems(Context context, int titleArrayId, int iconArrayId) {
        List<AdapterItem> list = new ArrayList<>();
        String[] titles = ResUtils.getStringArray(titleArrayId);
        Drawable[] icons = ResUtils.getDrawableArray(context, iconArrayId);
        for (int i = 0; i < titles.length; i++) {
            list.add(new AdapterItem(titles[i], icons[i]));
        }
        return list;
    }

}
