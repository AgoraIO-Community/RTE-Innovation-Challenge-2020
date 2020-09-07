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

package com.xuexiang.cloudblind.fragment.blinddate.room;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.cloudblind.R;
import com.xuexiang.cloudblind.adapter.entity.BlindRoom;
import com.xuexiang.cloudblind.core.rtc.AGEventHandler;
import com.xuexiang.cloudblind.core.rtc.Constant;
import com.xuexiang.cloudblind.core.rtc.ConstantApp;
import com.xuexiang.cloudblind.core.rtc.DuringCallEventHandler;
import com.xuexiang.cloudblind.core.rtc.entity.Message;
import com.xuexiang.cloudblind.core.rtc.entity.User;
import com.xuexiang.cloudblind.core.rtc.entity.UserStatusData;
import com.xuexiang.cloudblind.core.rtc.entity.VideoInfoData;
import com.xuexiang.cloudblind.utils.XToastUtils;
import com.xuexiang.cloudblind.widget.rtc.GridVideoViewContainer;
import com.xuexiang.cloudblind.widget.rtc.RecyclerItemClickListener;
import com.xuexiang.cloudblind.widget.rtc.adapter.InChannelMessageListAdapter;
import com.xuexiang.cloudblind.widget.rtc.adapter.MessageListDecoration;
import com.xuexiang.cloudblind.widget.rtc.adapter.SmallVideoViewAdapter;
import com.xuexiang.cloudblind.widget.rtc.adapter.SmallVideoViewDecoration;
import com.xuexiang.xaop.annotation.MainThread;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xrouter.annotation.AutoWired;
import com.xuexiang.xrouter.launcher.XRouter;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.actionbar.TitleUtils;
import com.xuexiang.xui.widget.button.SwitchIconView;
import com.xuexiang.xutil.XUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import butterknife.OnClick;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

/**
 * @author xuexiang
 * @since 2020/9/7 12:29 AM
 */
@Page
public class BlindDateRoomFragment extends RtcFragment implements DuringCallEventHandler {

    public static final String KEY_BLIND_ROOM = "key_blind_room";

    @AutoWired(name = KEY_BLIND_ROOM)
    BlindRoom mBlindRoom;

    TitleBar mTitleBar;

    public static final int LAYOUT_TYPE_DEFAULT = 0;
    public static final int LAYOUT_TYPE_SMALL = 1;

    private final HashMap<Integer, SurfaceView> mUidsList = new HashMap<>();
    public int mLayoutType = LAYOUT_TYPE_DEFAULT;
    private GridVideoViewContainer mGridVideoViewContainer;
    private RelativeLayout mSmallVideoViewDock;

    private volatile boolean mVideoMuted = false;
    private volatile boolean mAudioMuted = false;
    private volatile boolean mMixingAudio = false;

    private volatile int mAudioRouting = Constants.AUDIO_ROUTE_DEFAULT;

    private volatile boolean mFullScreen = false;

    private boolean mIsLandscape = false;

    private InChannelMessageListAdapter mMsgAdapter;
    private ArrayList<Message> mMsgList;

    private SmallVideoViewAdapter mSmallVideoViewAdapter;

    private final Handler mUIHandler = new Handler();

    private RecyclerView mSmallVideoRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_blind_date_room;
    }

    @Override
    protected void initArgs() {
        XRouter.getInstance().inject(this);
    }

    @Override
    protected TitleBar initTitle() {
        mTitleBar = findViewById(R.id.title_bar);
        TitleUtils.initTitleBarStyle(mTitleBar, mBlindRoom.Title, v -> popToBack());
        mTitleBar.setTitle(mBlindRoom.Title, mBlindRoom.RoomName, LinearLayout.VERTICAL);
        return mTitleBar;
    }

    @Override
    protected void initViews() {
        addEventHandler(this);
        // programmatically layout ui below of status bar/action bar
        LinearLayout eopsContainer = findViewById(R.id.extra_ops_container);
        RelativeLayout.MarginLayoutParams eofmp = (RelativeLayout.MarginLayoutParams) eopsContainer.getLayoutParams();
        eofmp.topMargin = getStatusBarHeight() + getActionBarHeight() + ThemeUtils.resolveDimension(getContext(), R.attr.xui_config_content_spacing_horizontal);

        final String encryptionKey = mBlindRoom.EncryptionKey;
        final String encryptionMode = ResUtils.getStringArray(R.array.encryption_mode_values)[mBlindRoom.EncryptionModeIndex];

        doConfigEngine(encryptionKey, encryptionMode);

        mGridVideoViewContainer = findViewById(R.id.grid_video_view_container);
        mGridVideoViewContainer.addOnItemTouchListener(mGridVideoItemClickListener);

        SurfaceView surfaceV = RtcEngine.CreateRendererView(XUI.getContext());
        preview(true, surfaceV, 0);
        surfaceV.setZOrderOnTop(false);
        surfaceV.setZOrderMediaOverlay(false);

        mUidsList.put(0, surfaceV);

        mGridVideoViewContainer.initViewContainer(getActivity(), 0, mUidsList, mIsLandscape);
        initMessageList();
        notifyMessageChanged(new Message(new User(0, null), "欢迎进入" + mBlindRoom.RoomName));

        joinChannel(mBlindRoom.ChannelName, config().mUid);

        optional();
    }


    RecyclerItemClickListener mGridVideoItemClickListener = new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            onBigVideoViewClicked(view, position);
        }

        @Override
        public void onItemLongClick(View view, int position) {

        }

        @Override
        public void onItemDoubleClick(View view, int position) {
            onBigVideoViewDoubleClicked(view, position);
        }
    });

    private void doConfigEngine(String encryptionKey, String encryptionMode) {
        VideoEncoderConfiguration.VideoDimensions videoDimension = ConstantApp.VIDEO_DIMENSIONS[getVideoEncResolutionIndex()];
        VideoEncoderConfiguration.FRAME_RATE videoFps = ConstantApp.VIDEO_FPS[getVideoEncFpsIndex()];
        configEngine(videoDimension, videoFps, encryptionKey, encryptionMode);
    }

    private void initMessageList() {
        mMsgList = new ArrayList<>();
        RecyclerView msgListView = findViewById(R.id.msg_list);

        mMsgAdapter = new InChannelMessageListAdapter(getActivity(), mMsgList);
        mMsgAdapter.setHasStableIds(true);
        msgListView.setAdapter(mMsgAdapter);
        msgListView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        msgListView.addItemDecoration(new MessageListDecoration());
    }

    private void notifyMessageChanged(Message msg) {
        mMsgList.add(msg);
        int MAX_MESSAGE_COUNT = 16;
        if (mMsgList.size() > MAX_MESSAGE_COUNT) {
            int toRemove = mMsgList.size() - MAX_MESSAGE_COUNT;
            for (int i = 0; i < toRemove; i++) {
                mMsgList.remove(i);
            }
        }

        mMsgAdapter.notifyDataSetChanged();
    }

    private void optional() {
        getActivity().setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
    }

    @MainThread
    @Override
    public void onUserJoined(int uid) {
        notifyMessageChanged(new Message(new User(0, null), "用户 " + (uid & 0xFFFFFFFFL) + " 加入"));
    }

    @MainThread
    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        doRenderRemoteUi(uid);
    }

    private void doRenderRemoteUi(final int uid) {
        if (getActivity().isFinishing()) {
            return;
        }

        if (mUidsList.containsKey(uid)) {
            return;
        }
        SurfaceView surfaceV = RtcEngine.CreateRendererView(XUtil.getContext());
        mUidsList.put(uid, surfaceV);

        boolean useDefaultLayout = mLayoutType == LAYOUT_TYPE_DEFAULT;

        surfaceV.setZOrderOnTop(true);
        surfaceV.setZOrderMediaOverlay(true);
        rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));

        if (useDefaultLayout) {
            switchToDefaultVideoView();
        } else {
            int bigBgUid = mSmallVideoViewAdapter == null ? uid : mSmallVideoViewAdapter.getExceptedUid();
            switchToSmallVideoView(bigBgUid);
        }
    }


    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {

    }

    @MainThread
    @Override
    public void onUserOffline(int uid, int reason) {
        doRemoveRemoteUi(uid);
    }


    private void doRemoveRemoteUi(final int uid) {
        if (getActivity().isFinishing()) {
            return;
        }

        Object target = mUidsList.remove(uid);
        if (target == null) {
            return;
        }

        int bigBgUid = -1;
        if (mSmallVideoViewAdapter != null) {
            bigBgUid = mSmallVideoViewAdapter.getExceptedUid();
        }
        if (mLayoutType == LAYOUT_TYPE_DEFAULT || uid == bigBgUid) {
            switchToDefaultVideoView();
        } else {
            switchToSmallVideoView(bigBgUid);
        }

        notifyMessageChanged(new Message(new User(0, null), "用户 " + (uid & 0xFFFFFFFFL) + " 离开"));
    }

    @MainThread
    @Override
    public void onExtraCallback(int type, Object... data) {
        doHandleExtraCallback(type, data);
    }

    private void doHandleExtraCallback(int type, Object... data) {
        if (getActivity().isFinishing()) {
            return;
        }
        int peerUid;
        boolean muted;
        switch (type) {
            case AGEventHandler.EVENT_TYPE_ON_USER_AUDIO_MUTED:
                peerUid = (Integer) data[0];
                muted = (boolean) data[1];

                if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                    HashMap<Integer, Integer> status = new HashMap<>();
                    status.put(peerUid, muted ? UserStatusData.AUDIO_MUTED : UserStatusData.DEFAULT_STATUS);
                    mGridVideoViewContainer.notifyUiChanged(mUidsList, config().mUid, status, null);
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_MUTED:
                peerUid = (Integer) data[0];
                muted = (boolean) data[1];

                doHideTargetView(peerUid, muted);

                break;

            case AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_STATS:
                IRtcEngineEventHandler.RemoteVideoStats stats = (IRtcEngineEventHandler.RemoteVideoStats) data[0];

                if (Constant.SHOW_VIDEO_INFO) {
                    if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                        mGridVideoViewContainer.addVideoInfo(stats.uid, new VideoInfoData(stats.width, stats.height, stats.delay, stats.rendererOutputFrameRate, stats.receivedBitrate));
                        int uid = config().mUid;
                        int profileIndex = getVideoEncResolutionIndex();
                        String resolution = ResUtils.getStringArray(R.array.string_array_resolutions)[profileIndex];
                        String fps = ResUtils.getStringArray(R.array.string_array_frame_rate)[profileIndex];

                        String[] rwh = resolution.split("x");
                        int width = Integer.valueOf(rwh[0]);
                        int height = Integer.valueOf(rwh[1]);

                        mGridVideoViewContainer.addVideoInfo(uid, new VideoInfoData(width > height ? width : height,
                                width > height ? height : width,
                                0, Integer.valueOf(fps), Integer.valueOf(0)));
                    }
                } else {
                    mGridVideoViewContainer.cleanVideoInfo();
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_SPEAKER_STATS:
                IRtcEngineEventHandler.AudioVolumeInfo[] infos = (IRtcEngineEventHandler.AudioVolumeInfo[]) data[0];

                if (infos.length == 1 && infos[0].uid == 0) { // local guy, ignore it
                    break;
                }

                if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                    HashMap<Integer, Integer> volume = new HashMap<>();

                    for (IRtcEngineEventHandler.AudioVolumeInfo each : infos) {
                        peerUid = each.uid;
                        int peerVolume = each.volume;

                        if (peerUid == 0) {
                            continue;
                        }
                        volume.put(peerUid, peerVolume);
                    }
                    mGridVideoViewContainer.notifyUiChanged(mUidsList, config().mUid, null, volume);
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_APP_ERROR:
                int subType = (int) data[0];

                if (subType == ConstantApp.AppError.NO_CONNECTION_ERROR) {
                    String msg = getString(R.string.msg_connection_error);
                    notifyMessageChanged(new Message(new User(0, null), msg));
                    XToastUtils.error(msg);
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_DATA_CHANNEL_MSG:
                peerUid = (Integer) data[0];
                final byte[] content = (byte[]) data[1];
                notifyMessageChanged(new Message(new User(peerUid, String.valueOf(peerUid)), new String(content)));

                break;

//            case AGEventHandler.EVENT_TYPE_ON_AGORA_MEDIA_ERROR:
//                int error = (int) data[0];
//                String description = (String) data[1];
//                notifyMessageChanged(new Message(new User(0, null), error + " " + description));
//                break;

            case AGEventHandler.EVENT_TYPE_ON_AUDIO_ROUTE_CHANGED:
                notifyHeadsetPlugged((int) data[0]);
                break;
            default:
                break;

        }
    }

    private void doHideTargetView(int targetUid, boolean hide) {
        HashMap<Integer, Integer> status = new HashMap<>();
        status.put(targetUid, hide ? UserStatusData.VIDEO_MUTED : UserStatusData.DEFAULT_STATUS);
        if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
            mGridVideoViewContainer.notifyUiChanged(mUidsList, targetUid, status, null);
        } else if (mLayoutType == LAYOUT_TYPE_SMALL) {
            UserStatusData bigBgUser = mGridVideoViewContainer.getItem(0);
            if (bigBgUser.mUid == targetUid) { // big background is target view
                mGridVideoViewContainer.notifyUiChanged(mUidsList, targetUid, status, null);
            } else { // find target view in small video view list
                mSmallVideoViewAdapter.notifyUiChanged(mUidsList, bigBgUser.mUid, status, null);
            }
        }
    }

    public void notifyHeadsetPlugged(final int routing) {
        mAudioRouting = routing;

        refreshAudioStatus();
    }

    private void refreshAudioStatus() {
        SwitchIconView iv = findViewById(R.id.switch_speaker_id);
        if (mAudioRouting == Constants.AUDIO_ROUTE_SPEAKERPHONE) {
            iv.setIconEnabled(true);
        } else {
            iv.setIconEnabled(false);
        }
    }

    private void onBigVideoViewClicked(View view, int position) {
        toggleFullscreen();
    }

    private void toggleFullscreen() {
        mFullScreen = !mFullScreen;
        mUIHandler.postDelayed(() -> {
            showOrHideCtrlViews(mFullScreen);
            showOrHideStatusBar(mFullScreen);
        }, 200);
    }

    private void showOrHideCtrlViews(boolean hide) {
        ViewUtils.setVisibility(mTitleBar, !hide);

        findViewById(R.id.extra_ops_container).setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
        findViewById(R.id.bottom_action_container).setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
        findViewById(R.id.msg_list).setVisibility(hide ? View.INVISIBLE : (Constant.DEBUG_INFO_ENABLED ? View.VISIBLE : View.INVISIBLE));
    }

    private void showOrHideStatusBar(boolean hide) {
        if (hide) {
            StatusBarUtils.fullScreen(getActivity());
        } else {
            StatusBarUtils.cancelFullScreen(getActivity());
        }
    }

    private void onBigVideoViewDoubleClicked(View view, int position) {
        if (mUidsList.size() < 2) {
            return;
        }

        UserStatusData user = mGridVideoViewContainer.getItem(position);
        int uid = (user.mUid == 0) ? config().mUid : user.mUid;

        if (mLayoutType == LAYOUT_TYPE_DEFAULT && mUidsList.size() != 1) {
            switchToSmallVideoView(uid);
        } else {
            switchToDefaultVideoView();
        }
    }

    private void switchToSmallVideoView(int bigBgUid) {
        HashMap<Integer, SurfaceView> slice = new HashMap<>(1);
        slice.put(bigBgUid, mUidsList.get(bigBgUid));
        Iterator<SurfaceView> iterator = mUidsList.values().iterator();
        while (iterator.hasNext()) {
            SurfaceView s = iterator.next();
            s.setZOrderOnTop(true);
            s.setZOrderMediaOverlay(true);
        }

        mUidsList.get(bigBgUid).setZOrderOnTop(false);
        mUidsList.get(bigBgUid).setZOrderMediaOverlay(false);

        mGridVideoViewContainer.initViewContainer(getActivity(), bigBgUid, slice, mIsLandscape);

        bindToSmallVideoView(bigBgUid);

        mLayoutType = LAYOUT_TYPE_SMALL;
    }

    private void switchToDefaultVideoView() {
        if (mSmallVideoViewDock != null) {
            mSmallVideoViewDock.setVisibility(View.GONE);
        }
        mGridVideoViewContainer.initViewContainer(getActivity(), config().mUid, mUidsList, mIsLandscape);

        mLayoutType = LAYOUT_TYPE_DEFAULT;
        boolean setRemoteUserPriorityFlag = false;
        int sizeLimit = mUidsList.size();
        if (sizeLimit > ConstantApp.MAX_PEER_COUNT + 1) {
            sizeLimit = ConstantApp.MAX_PEER_COUNT + 1;
        }
        for (int i = 0; i < sizeLimit; i++) {
            int uid = mGridVideoViewContainer.getItem(i).mUid;
            if (config().mUid != uid) {
                if (!setRemoteUserPriorityFlag) {
                    setRemoteUserPriorityFlag = true;
                    rtcEngine().setRemoteUserPriority(uid, Constants.USER_PRIORITY_HIGH);
                } else {
                    rtcEngine().setRemoteUserPriority(uid, Constants.USER_PRIORITY_NORANL);
                }
            }
        }
    }

    private void bindToSmallVideoView(int exceptUid) {
        if (mSmallVideoViewDock == null) {
            ViewStub stub = findViewById(R.id.small_video_view_dock);
            mSmallVideoViewDock = (RelativeLayout) stub.inflate();
        }

        mSmallVideoRecyclerView = findViewById(R.id.small_video_view_container);

        boolean create = false;

        if (mSmallVideoViewAdapter == null) {
            create = true;
            mSmallVideoViewAdapter = new SmallVideoViewAdapter(getActivity(), config().mUid, exceptUid, mUidsList);
            mSmallVideoViewAdapter.setHasStableIds(true);
        }
        mSmallVideoRecyclerView.setHasFixedSize(true);
        mSmallVideoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mSmallVideoRecyclerView.addItemDecoration(new SmallVideoViewDecoration());
        mSmallVideoRecyclerView.setAdapter(mSmallVideoViewAdapter);
        mSmallVideoRecyclerView.addOnItemTouchListener(mSmallVideoItemClickListener);

        mSmallVideoRecyclerView.setDrawingCacheEnabled(true);
        mSmallVideoRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

        if (!create) {
            mSmallVideoViewAdapter.setLocalUid(config().mUid);
            mSmallVideoViewAdapter.notifyUiChanged(mUidsList, exceptUid, null, null);
        }
        for (Integer tempUid : mUidsList.keySet()) {
            if (config().mUid != tempUid) {
                if (tempUid == exceptUid) {
                    rtcEngine().setRemoteUserPriority(tempUid, Constants.USER_PRIORITY_HIGH);
                } else {
                    rtcEngine().setRemoteUserPriority(tempUid, Constants.USER_PRIORITY_NORANL);
                }
            }
        }
        mSmallVideoRecyclerView.setVisibility(View.VISIBLE);
        mSmallVideoViewDock.setVisibility(View.VISIBLE);
    }

    RecyclerItemClickListener mSmallVideoItemClickListener = new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

        }

        @Override
        public void onItemLongClick(View view, int position) {

        }

        @Override
        public void onItemDoubleClick(View view, int position) {
            onSmallVideoViewDoubleClicked(view, position);
        }
    });

    private void onSmallVideoViewDoubleClicked(View view, int position) {
        switchToDefaultVideoView();
    }


    private int getVideoEncResolutionIndex() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(XUtil.getContext());
        int videoEncResolutionIndex = pref.getInt(ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_RESOLUTION, ConstantApp.DEFAULT_VIDEO_ENC_RESOLUTION_IDX);
        if (videoEncResolutionIndex > ConstantApp.VIDEO_DIMENSIONS.length - 1) {
            videoEncResolutionIndex = ConstantApp.DEFAULT_VIDEO_ENC_RESOLUTION_IDX;

            // save the new value
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_RESOLUTION, videoEncResolutionIndex);
            editor.apply();
        }
        return videoEncResolutionIndex;
    }

    private int getVideoEncFpsIndex() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(XUtil.getContext());
        int videoEncFpsIndex = pref.getInt(ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_FPS, ConstantApp.DEFAULT_VIDEO_ENC_FPS_IDX);
        if (videoEncFpsIndex > ConstantApp.VIDEO_FPS.length - 1) {
            videoEncFpsIndex = ConstantApp.DEFAULT_VIDEO_ENC_FPS_IDX;

            // save the new value
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_FPS, videoEncFpsIndex);
            editor.apply();
        }
        return videoEncFpsIndex;
    }


    @Override
    public void onDestroyView() {
        optionalDestroy();
        doLeaveChannel();
        removeEventHandler(this);
        mUidsList.clear();
        super.onDestroyView();
    }

    private void optionalDestroy() {
        if (mGridVideoViewContainer != null) {
            mGridVideoViewContainer.removeOnItemTouchListener(mGridVideoItemClickListener);
        }
        if (mSmallVideoRecyclerView != null) {
            mSmallVideoRecyclerView.removeOnItemTouchListener(mSmallVideoItemClickListener);
        }
    }

    private void doLeaveChannel() {
        leaveChannel(config().mChannel);
        preview(false, null, 0);
    }

    @SingleClick
    @OnClick({R.id.switch_speaker_id, R.id.switch_filter, R.id.btn_hangup, R.id.switch_video, R.id.switch_voice, R.id.iv_switch_camera, R.id.iv_mixing_audio})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.switch_speaker_id:
                rtcEngine().setEnableSpeakerphone(mAudioRouting != Constants.AUDIO_ROUTE_SPEAKERPHONE);
                break;
            case R.id.switch_filter:
                onFilterClicked(view);
                break;
            case R.id.btn_hangup:
                popToBack();
                break;
            case R.id.switch_video:
                onVideoMuteClicked(view);
                break;
            case R.id.switch_voice:
                onVoiceMuteClicked(view);
                break;
            case R.id.iv_switch_camera:
                rtcEngine().switchCamera();
                break;
            case R.id.iv_mixing_audio:
                onMixingAudioClicked(view);
                break;
            default:
                break;
        }
    }

    public void onFilterClicked(View view) {
        Constant.BEAUTY_EFFECT_ENABLED = !Constant.BEAUTY_EFFECT_ENABLED;

        if (Constant.BEAUTY_EFFECT_ENABLED) {
            setBeautyEffectParameters(Constant.BEAUTY_EFFECT_DEFAULT_LIGHTNESS, Constant.BEAUTY_EFFECT_DEFAULT_SMOOTHNESS, Constant.BEAUTY_EFFECT_DEFAULT_REDNESS);
            enablePreProcessor();
        } else {
            disablePreProcessor();
        }
        SwitchIconView iv = (SwitchIconView) view;
        iv.setIconEnabled(Constant.BEAUTY_EFFECT_ENABLED);
    }

    public void onVideoMuteClicked(View view) {
        if (mUidsList.size() == 0) {
            return;
        }

        SurfaceView surfaceV = getLocalView();
        if (surfaceV == null || surfaceV.getParent() == null) {
            return;
        }

        RtcEngine rtcEngine = rtcEngine();
        mVideoMuted = !mVideoMuted;

        if (mVideoMuted) {
            rtcEngine.disableVideo();
        } else {
            rtcEngine.enableVideo();
        }

        SwitchIconView iv = (SwitchIconView) view;

        iv.setIconEnabled(!mVideoMuted);

        hideLocalView(mVideoMuted);
    }

    private SurfaceView getLocalView() {
        for (HashMap.Entry<Integer, SurfaceView> entry : mUidsList.entrySet()) {
            if (entry.getKey() == 0 || entry.getKey() == config().mUid) {
                return entry.getValue();
            }
        }

        return null;
    }

    private void hideLocalView(boolean hide) {
        int uid = config().mUid;
        doHideTargetView(uid, hide);
    }

    public void onVoiceMuteClicked(View view) {
        if (mUidsList.size() == 0) {
            return;
        }

        RtcEngine rtcEngine = rtcEngine();
        rtcEngine.muteLocalAudioStream(mAudioMuted = !mAudioMuted);

        SwitchIconView iv = (SwitchIconView) view;

        iv.setIconEnabled(!mAudioMuted);
    }

    public void onMixingAudioClicked(View view) {
        if (mUidsList.size() == 0) {
            return;
        }

        mMixingAudio = !mMixingAudio;

        RtcEngine rtcEngine = rtcEngine();
        if (mMixingAudio) {
            rtcEngine.startAudioMixing(Constant.MIX_FILE_PATH, false, false, -1);
        } else {
            rtcEngine.stopAudioMixing();
        }

        ImageView iv = (ImageView) view;
        iv.setImageResource(mMixingAudio ? R.drawable.btn_audio_mixing : R.drawable.btn_audio_mixing_off);
    }

}
