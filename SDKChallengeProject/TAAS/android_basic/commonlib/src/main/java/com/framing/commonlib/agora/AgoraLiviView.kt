package com.framing.commonlib.agora

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import com.framing.baselib.TLog
import io.agora.rtc.Constants
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
 * Des  
 * Author Young
 * Date 
 */class AgoraLiviView :AgoraBaseView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
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


    fun channelProfile(){
        mEngine().setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)
    }
    /*
    * Constants.CLIENT_ROLE_BROADCASTER //主播
    * Constants.CLIENT_ROLE_AUDIENCE //观众
    * */
    fun clientRole(){
        mEngine().setClientRole(Constants.CLIENT_ROLE_BROADCASTER)
    }
    fun startPrepare(){
        TLog.log("TAAS_GROUP","startPrepare 11")
        channelProfile()
        clientRole()
        mEngine().enableVideo()
        val a= RtcEngine.CreateRendererView(context)
        a.setZOrderMediaOverlay(true)
        addView(a)
        val localVideoCanvas =
            VideoCanvas(a, VideoCanvas.RENDER_MODE_HIDDEN, 0)
        mEngine().setupLocalVideo(localVideoCanvas)
        mEngine().joinChannel("", "TAAS_GROUP", "", 0);
    }

    override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
        super.onFirstRemoteVideoDecoded(uid, width, height, elapsed)
        TLog.log("TAAS_GROUP","onFirstRemoteVideoDecoded")
        GlobalScope.launch {
            withContext(Dispatchers.Main){
                val a= RtcEngine.CreateRendererView(context)
                addView(a)
                mEngine().setupRemoteVideo(
                    VideoCanvas(
                        a,
                        VideoCanvas.RENDER_MODE_HIDDEN,
                        uid
                    )
                )
            }
        }
    }
}