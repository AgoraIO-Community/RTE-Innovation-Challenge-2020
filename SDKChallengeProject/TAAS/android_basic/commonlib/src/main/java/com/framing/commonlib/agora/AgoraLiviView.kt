package com.framing.commonlib.agora

<<<<<<< HEAD
import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import com.airbnb.lottie.LottieAnimationView
import com.framing.baselib.TLog
import com.framing.commonlib.R
import com.framing.commonlib.utils.DisplayUtils
import com.framing.commonlib.utils.ScreenUtils
import com.framing.commonlib.widget.StatusLayout
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
=======
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import com.framing.baselib.TLog
import io.agora.rtc.Constants
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

<<<<<<< HEAD

=======
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
/*
 * Des  
 * Author Young
 * Date 
 */class AgoraLiviView :AgoraBaseView {
<<<<<<< HEAD
    constructor(context: Context) : super(context){
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AgoraLiviView)
        isMissShow=typedArray.getBoolean(R.styleable.AgoraLiviView_attentionShow,true)
    }
=======
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
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

<<<<<<< HEAD
    private  var  loadingLayout:StatusLayout?=null
    private  var  lottieView:LottieAnimationView?=null
    private var isMissShow:Boolean?=null//提示箭头显示
    private var status:IRtcStats?=null//给上层view

    @SuppressLint("ResourceAsColor")
    private fun initView(){
        removeAllViews()
        //loading view
        if(loadingLayout==null) {
            loadingLayout = StatusLayout(context)
        }
        addView(loadingLayout, LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT))
        loadingLayout?.isClickable=false
        if(isMissShow!!) {
            //提示上滑
            if (lottieView == null) {
                lottieView = LottieAnimationView(context)
            }
            val params = LayoutParams(DisplayUtils.dp2px(60f), DisplayUtils.dp2px(60f))
            params.gravity = Gravity.RIGHT
            params.topMargin =
                ScreenUtils.getScreenHeight() / 2 - params.height - DisplayUtils.dp2px(20f)
            addView(lottieView, params)
            lottieView?.setAnimation("arrow_miss.json")
            lottieView?.repeatCount = 2
            lottieView?.rotation = 180f
            lottieView?.playAnimation()
            lottieView?.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    lottieView?.visibility = View.GONE
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationStart(p0: Animator?) {
                }
            })
        }
    }
    private fun channelProfile(){
=======

    fun channelProfile(){
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
        mEngine().setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)
    }
    /*
    * Constants.CLIENT_ROLE_BROADCASTER //主播
    * Constants.CLIENT_ROLE_AUDIENCE //观众
    * */
<<<<<<< HEAD
    private fun clientRole(type:Int){
        mEngine().setClientRole(type)
    }
    fun startPrepare(type:Int,status:IRtcStats){
        this.status=status
        initEngine()
        initView()
        loadingLayout?.statusType(StatusLayout.StatusType.LOADING)
        TLog.log("TAAS_GROUP","startPrepare 11")
        channelProfile()
        clientRole(type)
        mEngine().joinChannel("", "TAAS_GROUP", "", 0)
        if(type==Constants.CLIENT_ROLE_BROADCASTER){
            mEngine().enableVideo()
            setupLocalVideo()
        }
    }
    /*
    * 看人直播
    * */
   override fun setupRemoteVideo(uid: Int) {
        TLog.log("etupremotevideo","$uid")
        job= viewScope.launch {
            withContext(Dispatchers.Main){
                loadingLayout?.statusType(StatusLayout.StatusType.HIDE)
                // 创建一个 SurfaceView 对象。
                val mRemoteView: TextureView
                mRemoteView = RtcEngine.CreateTextureView(context)
                val videoLayoutParams = LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                addView(mRemoteView, videoLayoutParams)
                // 设置远端视图。
                mEngine().setupRemoteVideo(
                    VideoCanvas(
                        mRemoteView,
=======
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
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
                        VideoCanvas.RENDER_MODE_HIDDEN,
                        uid
                    )
                )
            }
        }
    }
<<<<<<< HEAD

    override fun destoryView() {
        removeAllViews()
    }

    /*
    * 自己直播
    * */
    private fun setupLocalVideo() {
        // 创建 SurfaceView 对象。
        val mLocalView: TextureView
        mLocalView = RtcEngine.CreateTextureView(context)
//        mLocalView.setZOrderMediaOverlay(true)
        addView(mLocalView)
        // 设置本地视图。
        val localVideoCanvas = VideoCanvas(
            mLocalView,
            VideoCanvas.RENDER_MODE_HIDDEN,
            0
        )
        mEngine().setupLocalVideo(localVideoCanvas)
    }

    override fun onRtcStats(stats: IRtcEngineEventHandler.RtcStats?) {
        super.onRtcStats(stats)
        status?.run {
            statusResult(stats)
        }
        viewScope.launch {
            withContext(Dispatchers.Main){
                if(stats?.users==1){
                    loadingLayout?.attentionText(resources.getString(R.string.app_attention_live))
                    loadingLayout?.statusType(StatusLayout.StatusType.ATTENTION)
                }
            }
        }
    }
    interface IRtcStats{
        fun statusResult(stats: IRtcEngineEventHandler.RtcStats?)
    }
=======
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
}