package com.framing.commonlib.agora

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.framing.baselib.TLog
import com.framing.commonlib.R
import com.framing.commonlib.agora.rtc.AgoraConstants.VIDEO_DIMENSIONS
import com.framing.commonlib.agora.rtc.AgoraConstants.VIDEO_MIRROR_MODES
import com.framing.commonlib.agora.rtc.AgoraEventHandler
import com.framing.commonlib.agora.rtc.EngineConfig
import com.framing.commonlib.agora.rtc.EventHandler
import com.framing.commonlib.agora.status.StatsManager
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoEncoderConfiguration

/*
 * Des  
 * Author Young
 * Date 
 */
open class AgoraBaseView :FrameLayout ,EventHandler,LifecycleObserver {
    constructor(context: Context) : super(context){
        initEngine()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        initEngine()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    override fun onRemoteAudioStats(stats: IRtcEngineEventHandler.RemoteAudioStats?) {
        TLog.log("TAAS_GROUP","onRemoteAudioStats")
    }

    override fun onRtcStats(stats: IRtcEngineEventHandler.RtcStats?) {
        TLog.log("TAAS_GROUP","onRtcStats")
    }

    override fun onUserJoined(uid: Int, elapsed: Int) {
        TLog.log("TAAS_GROUP","onUserJoined")
    }

    override fun onLocalVideoStats(stats: IRtcEngineEventHandler.LocalVideoStats?) {
        TLog.log("TAAS_GROUP","onLocalVideoStats")
    }

    override fun onUserOffline(uid: Int, reason: Int) {
        TLog.log("TAAS_GROUP","onUserOffline")
    }

    override fun onRemoteVideoStats(stats: IRtcEngineEventHandler.RemoteVideoStats?) {
        TLog.log("TAAS_GROUP","onRemoteVideoStats")
    }

    override fun onNetworkQuality(uid: Int, txQuality: Int, rxQuality: Int) {
        TLog.log("TAAS_GROUP","onNetworkQuality")
    }

    override fun onLastmileProbeResult(result: IRtcEngineEventHandler.LastmileProbeResult?) {
        TLog.log("TAAS_GROUP","onLastmileProbeResult")
    }

    override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
        TLog.log("TAAS_GROUP","onJoinChannelSuccess")
    }

    override fun onLastmileQuality(quality: Int) {
        TLog.log("TAAS_GROUP","onLastmileQuality")
    }

    override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
        TLog.log("TAAS_GROUP","onFirstRemoteVideoDecoded")
    }

    override fun onLeaveChannel(stats: IRtcEngineEventHandler.RtcStats?) {
        TLog.log("TAAS_GROUP","onLeaveChannel")
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){

    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestory(){
        RtcEngine.destroy()
    }

    private var mRtcEngine:RtcEngine?=null
    private val mGlobalConfig: EngineConfig = EngineConfig()
    private val mHandler: AgoraEventHandler = AgoraEventHandler()
    private val mStatsManager: StatsManager = StatsManager()

    private fun initEngine(){
        TLog.log("TAAS_GROUP","initEngine")
        try {
            mRtcEngine = RtcEngine.create(
                context,
                context.getString(R.string.private_app_id),
                mHandler
            )
            // Sets the channel profile of the Agora RtcEngine.
            // The Agora RtcEngine differentiates channel profiles and applies different optimization algorithms accordingly. For example, it prioritizes smoothness and low latency for a video call, and prioritizes video quality for a video broadcast.
            mRtcEngine?.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)
            mRtcEngine?.enableVideo()
        } catch (e: Exception) {
            TLog.log("TAAS_GROUP","initEngine$e")
            e.printStackTrace()
        }
        initConfig()
        configVideo()
    }
    private fun initConfig(){
        mHandler.addHandler(this)
    }
    fun mEngine():RtcEngine{
        return mRtcEngine!!
    }
    private fun configVideo() {
        val configuration =
            VideoEncoderConfiguration(
                VIDEO_DIMENSIONS.get(5),
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
            )
        configuration.mirrorMode = VIDEO_MIRROR_MODES.get(0)
        mEngine().setVideoEncoderConfiguration(configuration)
    }

}