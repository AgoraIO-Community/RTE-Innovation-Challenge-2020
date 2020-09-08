package com.framing.commonlib.agora

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.SurfaceView
import android.widget.FrameLayout
import android.widget.RelativeLayout
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
import com.framing.commonlib.widget.BaseObserverView
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoEncoderConfiguration

/*
 * Des  
 * Author Young
 * Date 
 */
abstract class AgoraBaseView :BaseObserverView ,EventHandler,LifecycleObserver {
    constructor(context: Context) : super(context){
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
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
        TLog.log("TAAS_GROUP","onRemoteAudioStats${stats}")
    }

    override fun onRtcStats(stats: IRtcEngineEventHandler.RtcStats?) {
        TLog.log("TAAS_GROUP","onRtcStats${stats}")
    }

    override fun onUserJoined(uid: Int, elapsed: Int) {
        TLog.log("TAAS_GROUP","onUserJoined${elapsed}")
    }

    override fun onLocalVideoStats(stats: IRtcEngineEventHandler.LocalVideoStats?) {
        TLog.log("TAAS_GROUP","onLocalVideoStats${stats}")
    }

    override fun onUserOffline(uid: Int, reason: Int) {
        TLog.log("TAAS_GROUP","onUserOffline${reason}")
    }

    override fun onRemoteVideoStats(stats: IRtcEngineEventHandler.RemoteVideoStats?) {
        TLog.log("TAAS_GROUP","onRemoteVideoStats${stats}")
    }
    override fun onNetworkQuality(uid: Int, txQuality: Int, rxQuality: Int) {
        TLog.log("TAAS_GROUP","onNetworkQuality$txQuality---$rxQuality")
    }

    override fun onLastmileProbeResult(result: IRtcEngineEventHandler.LastmileProbeResult?) {
        TLog.log("TAAS_GROUP","onLastmileProbeResult${result}")
    }

    override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
        TLog.log("TAAS_GROUP","onJoinChannelSuccess${channel}--${elapsed}")
    }

    override fun onLastmileQuality(quality: Int) {
        TLog.log("TAAS_GROUP","onLastmileQuality")
    }

    override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
        TLog.log("TAAS_GROUP","onFirstRemoteVideoDecoded${elapsed}")
        setupRemoteVideo(uid)
    }

    override fun onLeaveChannel(stats: IRtcEngineEventHandler.RtcStats?) {
        TLog.log("TAAS_GROUP","onLeaveChannel${stats}")
    }

    /*
    *监听频道中直播状态
    * channel	频道名。
    *uid	远端用户的 ID。
    *oldState	之前的订阅状态。
    *newState	当前的订阅状态。
    *elapseSinceLastState	两次状态变化时间间隔（毫秒）。
    * */
    override fun onVideoSubscribeStateChanged(s: String?, i: Int, i1: Int, i2: Int, i3: Int) {
        TLog.log("TAAS_GROUP","onVIdeoSubstate$i2")
    }
    /*
    * uid	发生视频状态改变的远端用户 ID
    *
    * state	远端视频流状态:
    * REMOTE_VIDEO_STATE_STOPPED(0)：远端视频默认初始状态。在
    * REMOTE_VIDEO_STATE_STARTING(1)：本地用户已接收远端视频首包
    * REMOTE_VIDEO_STATE_DECODING(2)：远端视频流正在解码，正常播放。在
    * REMOTE_VIDEO_STATE_FROZEN(3)：远端视频流卡顿。在
    * REMOTE_VIDEO_STATE_FAILED(4)：远端视频流播放失败。在
    *
    * reason	远端视频流状态改变的具体原因：
    * REMOTE_VIDEO_STATE_REASON_INTERNAL(0)：内部原因
    * REMOTE_VIDEO_STATE_REASON_NETWORK_CONGESTION(1)：网络阻塞
    * REMOTE_VIDEO_STATE_REASON_NETWORK_RECOVERY(2)：网络恢复正常
    * REMOTE_VIDEO_STATE_REASON_LOCAL_MUTED(3)：本地用户停止接收远端视频流或本地用户禁用视频模块]
    * REMOTE_VIDEO_STATE_REASON_LOCAL_UNMUTED(4)：本地用户恢复接收远端视频流或本地用户启动视频模块
    * REMOTE_VIDEO_STATE_REASON_REMOTE_MUTED(5)：远端用户停止发送视频流或远端用户禁用视频模块
    * REMOTE_VIDEO_STATE_REASON_REMOTE_UNMUTED(6)：远端用户恢复发送视频流或远端用户启用视频模块
    * REMOTE_VIDEO_STATE_REASON_REMOTE_OFFLINE(7)：远端用户离开频道
    * REMOTE_VIDEO_STATE_REASON_AUDIO_FALLBACK(8)：远端视频流已回退为音频流
    * REMOTE_VIDEO_STATE_REASON_AUDIO_FALLBACK_RECOVERY(9)：回退的远端音频流恢复为视频流
    *
    * elapsed	从本地用户调用 joinChannel 方法到发生本事件经历的时间，单位为 ms
    * */
    override fun onRemoteVideoStateChanged(i: Int, i1: Int, i2: Int, i3: Int) {
        TLog.log("TAAS_GROUP","onRemoteVideoStateChanged$i1---$i2")
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){

    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestory(){
        mRtcEngine?.leaveChannel()
        RtcEngine.destroy()
        destoryView()
    }

    private var mRtcEngine:RtcEngine?=null
    private val mGlobalConfig: EngineConfig = EngineConfig()
    private val mHandler: AgoraEventHandler = AgoraEventHandler()
    private val mStatsManager: StatsManager = StatsManager()

    fun initEngine(){
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
    abstract  fun setupRemoteVideo(uid: Int)
    abstract fun destoryView()
}