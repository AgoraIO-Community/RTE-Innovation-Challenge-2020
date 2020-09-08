package com.framing.commonlib.agora.rtc;

import com.framing.baselib.TLog;

import java.util.ArrayList;

import io.agora.rtc.IRtcEngineEventHandler;

public class AgoraEventHandler extends IRtcEngineEventHandler {
    private ArrayList<EventHandler> mHandler = new ArrayList<>();

    public void addHandler(EventHandler handler) {
        mHandler.add(handler);
    }

    public void removeHandler(EventHandler handler) {
        mHandler.remove(handler);
    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        for (EventHandler handler : mHandler) {
            handler.onJoinChannelSuccess(channel, uid, elapsed);
        }
    }

    @Override
    public void onLeaveChannel(RtcStats stats) {
        for (EventHandler handler : mHandler) {
            handler.onLeaveChannel(stats);
        }
    }

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        for (EventHandler handler : mHandler) {
            handler.onFirstRemoteVideoDecoded(uid, width, height, elapsed);
        }
    }

    @Override
    public void onUserJoined(int uid, int elapsed) {
        for (EventHandler handler : mHandler) {
            handler.onUserJoined(uid, elapsed);
        }
    }

    @Override
    public void onUserOffline(int uid, int reason) {
        for (EventHandler handler : mHandler) {
            handler.onUserOffline(uid, reason);
        }
    }

    @Override
    public void onLocalVideoStats(IRtcEngineEventHandler.LocalVideoStats stats) {
        for (EventHandler handler : mHandler) {
            handler.onLocalVideoStats(stats);
        }
    }

    @Override
    public void onRtcStats(IRtcEngineEventHandler.RtcStats stats) {
        TLog.log("TAAS_GROUP_onRtcStats",""+stats.users);
        for (EventHandler handler : mHandler) {
            handler.onRtcStats(stats);
        }
    }

    @Override
    public void onNetworkQuality(int uid, int txQuality, int rxQuality) {
        for (EventHandler handler : mHandler) {
            handler.onNetworkQuality(uid, txQuality, rxQuality);
        }
    }

    @Override
    public void onRemoteVideoStats(IRtcEngineEventHandler.RemoteVideoStats stats) {
        TLog.log("TAAS_GROUP_onRemoteVideoStats",stats.toString());
        for (EventHandler handler : mHandler) {
            handler.onRemoteVideoStats(stats);
        }
    }

    @Override
    public void onRemoteAudioStats(IRtcEngineEventHandler.RemoteAudioStats stats) {
        for (EventHandler handler : mHandler) {
            handler.onRemoteAudioStats(stats);
        }
    }

    @Override
    public void onLastmileQuality(int quality) {
        for (EventHandler handler : mHandler) {
            handler.onLastmileQuality(quality);
        }
    }

    @Override
    public void onLastmileProbeResult(IRtcEngineEventHandler.LastmileProbeResult result) {
        for (EventHandler handler : mHandler) {
            handler.onLastmileProbeResult(result);
        }
    }

    @Override
    public void onFirstRemoteVideoFrame(int i, int i1, int i2, int i3) {
        super.onFirstRemoteVideoFrame(i, i1, i2, i3);
        TLog.log("TAAS_GROUP_onFirstRemoteVideoFrame",i+"__"+i1+"__"+i2);
    }

    @Override
    public void onFirstLocalVideoFrame(int i, int i1, int i2) {
        super.onFirstLocalVideoFrame(i, i1, i2);
        TLog.log("TAAS_GROUP_onFirstLocalVideoFrame",i+"__"+i1+"__"+i2);
    }

    @Override
    public void onRemoteVideoStateChanged(int i, int i1, int i2, int i3) {
        super.onRemoteVideoStateChanged(i, i1, i2, i3);
        for (EventHandler handler : mHandler) {
            handler.onRemoteVideoStateChanged(i, i1, i2, i3);
        }
    }

    @Override
    public void onVideoSubscribeStateChanged(String s, int i, int i1, int i2, int i3) {
        super.onVideoSubscribeStateChanged(s, i, i1, i2, i3);
        for (EventHandler handler : mHandler) {
            handler.onVideoSubscribeStateChanged(s, i, i1, i2, i3);
        }
    }
}
