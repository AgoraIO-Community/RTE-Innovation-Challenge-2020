package com.framing.commonlib.agora.rtc;

import io.agora.rtc.IRtcEngineEventHandler;

public interface EventHandler {
    void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed);

    void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats);

    void onJoinChannelSuccess(String channel, int uid, int elapsed);

    void onUserOffline(int uid, int reason);

    void onUserJoined(int uid, int elapsed);

    void onLastmileQuality(int quality);

    void onLastmileProbeResult(IRtcEngineEventHandler.LastmileProbeResult result);

    void onLocalVideoStats(IRtcEngineEventHandler.LocalVideoStats stats);

    void onRtcStats(IRtcEngineEventHandler.RtcStats stats);

    void onNetworkQuality(int uid, int txQuality, int rxQuality);

    void onRemoteVideoStats(IRtcEngineEventHandler.RemoteVideoStats stats);

    void onRemoteAudioStats(IRtcEngineEventHandler.RemoteAudioStats stats);

//    void onFirstRemoteVideoFrame(int i, int i1, int i2, int i3);
//
//    void onFirstLocalVideoFrame(int i, int i1, int i2);
//
    void onRemoteVideoStateChanged(int i, int i1, int i2, int i3);

    void onVideoSubscribeStateChanged(String s, int i, int i1, int i2, int i3);

}
