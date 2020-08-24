package com.framing.commonlib.agora

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.AttributeSet
import android.view.SurfaceView
import android.view.TextureView
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.framing.commonlib.utils.DisplayUtils
import io.agora.mediaplayer.*
import io.agora.mediaplayer.Constants.PLAYER_RENDER_MODE_FIT
import io.agora.mediaplayer.Constants.PLAYER_RENDER_MODE_HIDDEN
import io.agora.mediaplayer.data.AudioFrame
import io.agora.mediaplayer.data.VideoFrame
import io.agora.mediaplayer.internal.AgoraMediaPlayer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/*
 * Des  声网媒体播放器组件
 * Author Young
 * Date 
 */class AgoraMediaKit : TextureView, MediaPlayerObserver, AudioFrameObserver , VideoFrameObserver {
    constructor(context: Context?) : super(context!!){
        initConfig()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs){
        initConfig()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    )

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context!!, attrs, defStyleAttr, defStyleRes)


    private var playerKit:AgoraMediaPlayerKit?=null


    private fun initConfig(){
        createKit()
    }
    private fun createKit(){
        playerKit=AgoraMediaPlayerKit(context)
        playerKit?.registerPlayerObserver(this)//player 回调
        playerKit?.registerAudioFrameObserver(this)//audio 回调
        playerKit?.registerVideoFrameObserver(this)//video 回调
        playerKit?.setView(this@AgoraMediaKit)
        playerKit?.setRenderMode(PLAYER_RENDER_MODE_HIDDEN)
        GlobalScope.launch {
//            Thread.sleep(6000)
//            startWith("https://video.beilezx.com/upload%2Fa%2F2020%2F0426%2F1587875341249_15.mp4")
        }
    }

    /*
    * 方法打开媒体资源。媒体资源路径可以为网络路径或本地路径，支持绝对路径和相对路径。
    * url
    * */
    fun startWith(url:String){
        playerKit?.open(url,0)
    }
    /*
    * 结束
    * */
    fun stop(){
        playerKit?.stop()
        playerKit?.unregisterAudioFrameObserver(this)
        playerKit?.unregisterPlayerObserver(this)
        playerKit?.unregisterVideoFrameObserver(this)
        playerKit?.destroy()
    }
    /*
    * start MediaPlayerObserver
    *registerPlayerObserver 方法注册一个播放器的观测器对象（playerObserver），监听以下播放事件：
    *onPositionChanged，报告当前播放进度
    *onPlayerStateChanged，报告播放状态改变
    *onPlayerEvent，报告定位播放状态
    *onMetaData，报告媒体附属信息（metadata）的接收
    * */
    override fun onPlayerStateChanged(
        p0: Constants.MediaPlayerState?,
        p1: Constants.MediaPlayerError?
    ) {
        when(p0){
            Constants.MediaPlayerState.PLAYER_STATE_OPEN_COMPLETED->{
                playerKit?.play()
            }
        }
    }

    override fun onPositionChanged(p0: Long) {
    }

    override fun onPlayerEvent(p0: Constants.MediaPlayerEvent?) {
    }

    override fun onMetaData(p0: Constants.MediaPlayerMetadataType?, p1: ByteArray?) {
    }
    //*end MediaPlayerObserver */

    /*
    * =======start======
    * AudioFrameObserver   VideoFrameObserver
    *
    * */
    override fun onFrame(p0: AudioFrame?) {
    }

    override fun onFrame(p0: VideoFrame?) {
    }
    //=======end========
}

