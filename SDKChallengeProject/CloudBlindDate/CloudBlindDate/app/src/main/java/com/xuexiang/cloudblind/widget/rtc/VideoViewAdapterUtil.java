package com.xuexiang.cloudblind.widget.rtc;

import android.content.Context;
import android.graphics.Color;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.xuexiang.cloudblind.R;
import com.xuexiang.cloudblind.core.rtc.Constant;
import com.xuexiang.cloudblind.core.rtc.entity.UserStatusData;
import com.xuexiang.cloudblind.core.rtc.entity.VideoInfoData;
import com.xuexiang.cloudblind.utils.ViewUtil;
import com.xuexiang.cloudblind.widget.rtc.adapter.VideoUserStatusHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class VideoViewAdapterUtil {

    public static void composeDataItem1(final ArrayList<UserStatusData> users, HashMap<Integer, SurfaceView> uids, int localUid) {
        for (HashMap.Entry<Integer, SurfaceView> entry : uids.entrySet()) {
            SurfaceView surfaceV = entry.getValue();
            surfaceV.setZOrderOnTop(false);
            surfaceV.setZOrderMediaOverlay(false);
            searchUidsAndAppend(users, entry, localUid, UserStatusData.DEFAULT_STATUS, UserStatusData.DEFAULT_VOLUME, null);
        }

        removeNotExisted(users, uids, localUid);
    }

    private static void removeNotExisted(ArrayList<UserStatusData> users, HashMap<Integer, SurfaceView> uids, int localUid) {
        Iterator<UserStatusData> it = users.iterator();
        while (it.hasNext()) {
            UserStatusData user = it.next();
            if (uids.get(user.mUid) == null && user.mUid != localUid) {
                it.remove();
            }
        }
    }

    private static void searchUidsAndAppend(ArrayList<UserStatusData> users, HashMap.Entry<Integer, SurfaceView> entry,
                                            int localUid, Integer status, int volume, VideoInfoData i) {
        if (entry.getKey() == 0 || entry.getKey() == localUid) {
            boolean found = false;
            for (UserStatusData user : users) {
                if ((user.mUid == entry.getKey() && user.mUid == 0) || user.mUid == localUid) { // first time
                    user.mUid = localUid;
                    if (status != null) {
                        user.mStatus = status;
                    }
                    user.mVolume = volume;
                    user.setVideoInfo(i);
                    found = true;
                    break;
                }
            }
            if (!found) {
                users.add(0, new UserStatusData(localUid, entry.getValue(), status, volume, i));
            }
        } else {
            boolean found = false;
            for (UserStatusData user : users) {
                if (user.mUid == entry.getKey()) {
                    if (status != null) {
                        user.mStatus = status;
                    }
                    user.mVolume = volume;
                    user.setVideoInfo(i);
                    found = true;
                    break;
                }
            }
            if (!found) {
                users.add(new UserStatusData(entry.getKey(), entry.getValue(), status, volume, i));
            }
        }
    }

    public static void composeDataItem(final ArrayList<UserStatusData> users, HashMap<Integer, SurfaceView> uids,
                                       int localUid,
                                       HashMap<Integer, Integer> status,
                                       HashMap<Integer, Integer> volume,
                                       HashMap<Integer, VideoInfoData> video) {
        composeDataItem(users, uids, localUid, status, volume, video, 0);
    }

    public static void composeDataItem(final ArrayList<UserStatusData> users, HashMap<Integer, SurfaceView> uids,
                                       int localUid,
                                       HashMap<Integer, Integer> status,
                                       HashMap<Integer, Integer> volume,
                                       HashMap<Integer, VideoInfoData> video, int uidExcepted) {
        for (HashMap.Entry<Integer, SurfaceView> entry : uids.entrySet()) {
            int uid = entry.getKey();

            if (uid == uidExcepted && uidExcepted != 0) {
                continue;
            }

            boolean local = uid == 0 || uid == localUid;

            Integer s = null;
            if (status != null) {
                s = status.get(uid);
                if (local && s == null) { // check again
                    s = status.get(uid == 0 ? localUid : 0);
                }
            }
            Integer v = null;
            if (volume != null) {
                v = volume.get(uid);
                if (local && v == null) { // check again
                    v = volume.get(uid == 0 ? localUid : 0);
                }
            }
            if (v == null) {
                v = UserStatusData.DEFAULT_VOLUME;
            }
            VideoInfoData i;
            if (video != null) {
                i = video.get(uid);
                if (local && i == null) { // check again
                    i = video.get(uid == 0 ? localUid : 0);
                }
            } else {
                i = null;
            }
            searchUidsAndAppend(users, entry, localUid, s, v, i);
        }

        removeNotExisted(users, uids, localUid);
    }

    public static void renderExtraData(Context context, UserStatusData user, VideoUserStatusHolder myHolder) {
        if (user.mStatus != null) {
            if ((user.mStatus & UserStatusData.VIDEO_MUTED) != 0) {
                myHolder.mAvatar.setVisibility(View.VISIBLE);
                myHolder.mMaskView.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
            } else {
                myHolder.mAvatar.setVisibility(View.GONE);
                myHolder.mMaskView.setBackgroundColor(Color.TRANSPARENT);
            }

            if ((user.mStatus & UserStatusData.AUDIO_MUTED) != 0) {
                myHolder.mIndicator.setImageResource(R.drawable.icon_muted);
                myHolder.mIndicator.setVisibility(View.VISIBLE);
                myHolder.mIndicator.setTag(System.currentTimeMillis());
                return;
            } else {
                myHolder.mIndicator.setTag(null);
                myHolder.mIndicator.setVisibility(View.INVISIBLE);
            }
        }

        Object tag = myHolder.mIndicator.getTag();
        if (tag != null && System.currentTimeMillis() - (Long) tag < 1500) { // workaround for audio volume comes just later than mute
            return;
        }

        int volume = user.mVolume;

        if (volume > 0) {
            myHolder.mIndicator.setImageResource(R.drawable.icon_speaker);
            myHolder.mIndicator.setVisibility(View.VISIBLE);
        } else {
            myHolder.mIndicator.setVisibility(View.INVISIBLE);
        }

//        if (Constant.SHOW_VIDEO_INFO && user.getVideoInfoData() != null) {
//            VideoInfoData videoInfo = user.getVideoInfoData();
//            myHolder.mMetaData.setText(ViewUtil.composeVideoInfoString(context, videoInfo));
//            myHolder.mVideoInfo.setVisibility(View.VISIBLE);
//        } else {
//            myHolder.mVideoInfo.setVisibility(View.GONE);
//        }
        myHolder.mVideoInfo.setVisibility(View.GONE);
    }

    public static void stripView(SurfaceView view) {
        ViewParent parent = view.getParent();
        if (parent != null) {
            ((FrameLayout) parent).removeView(view);
        }
    }
}
