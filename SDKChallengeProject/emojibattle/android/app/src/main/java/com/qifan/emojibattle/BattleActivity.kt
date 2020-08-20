/**
 * MIT License
 * <p>
 * Copyright (C) 2020 by Qifan YANG (@underwindfall)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.qifan.emojibattle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.qifan.emojibattle.databinding.ActivityBattleBinding
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.video.VideoCanvas
import io.agora.rtc2.video.VideoEncoderConfiguration
import kotlin.properties.Delegates.notNull

class BattleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBattleBinding
    private val localSurfaceView get() = binding.localSurfaceView
    private val remoteSurfaceView get() = binding.remoteSurfaceView

    private var channel: String by notNull()
    private val token = BuildConfig.Token
    private val appId = BuildConfig.AppId
    private lateinit var rtcEngine: RtcEngine

    companion object {
        private const val CHANNEL = "Channel"

        @JvmStatic
        fun Activity.startBattleActivity(channel: String) {
            startActivity(
                Intent(this, BattleActivity::class.java)
                    .putExtra(CHANNEL, channel)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBattleBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_battle)
        parseIntents()
        initAgoraEngineAndJoinChannel()
    }

    private fun parseIntents() {
        val channel = intent.getStringExtra(CHANNEL)
        requireNotNull(channel)
    }

    private fun initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine()
        setupVideoProfile()
        setupLocalVideo()
        joinChannel()
    }

    private fun initializeAgoraEngine() {
        try {
            rtcEngine = RtcEngine.create(
                applicationContext,
                appId,
                object : IRtcEngineEventHandler() {
                    override fun onFirstRemoteVideoDecoded(
                        userId: Int,
                        width: Int,
                        height: Int,
                        elapsed: Int
                    ) {
                        super.onFirstRemoteVideoDecoded(userId, width, height, elapsed)
                        runOnUiThread { setRemoteVideo(userId) }
                    }
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupVideoProfile() {
        rtcEngine.enableVideo()
        rtcEngine.setVideoEncoderConfiguration(
            VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
            )
        )
    }

    private fun setupLocalVideo() {
        val surfaceView = RtcEngine.CreateRendererView(applicationContext)
        localSurfaceView.addView(surfaceView)
        rtcEngine.setupLocalVideo(
            VideoCanvas(
                surfaceView,
                VideoCanvas.RENDER_MODE_FIT,
                VideoCanvas.RENDER_SOURCE_CAMERA,
                0
            )
        )
        surfaceView.setZOrderMediaOverlay(true)
    }

    private fun joinChannel() {
        // if you do not specify the uid, Agora will assign one.
        val channelMediaOptions = ChannelMediaOptions()
        channelMediaOptions.publishCameraTrack = true
        channelMediaOptions.autoSubscribeVideo = true
        channelMediaOptions.publishMediaPlayerVideoTrack = false
        channelMediaOptions.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
        channelMediaOptions.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
        rtcEngine.joinChannel(token, channel, 0, channelMediaOptions)
        rtcEngine.startPreview()
    }

    private fun leaveChannel() {
        rtcEngine.leaveChannel()
    }

    private fun setRemoteVideo(uid: Int) {
        if (remoteSurfaceView.childCount >= 1) return
        val surfaceView = RtcEngine.CreateRendererView(applicationContext)
        remoteSurfaceView.addView(surfaceView)
        surfaceView.setZOrderMediaOverlay(true)
        rtcEngine.setupRemoteVideo(
            VideoCanvas(
                surfaceView,
                VideoCanvas.RENDER_MODE_FIT,
                VideoCanvas.RENDER_SOURCE_CAMERA,
                uid
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        rtcEngine.stopPreview()
        leaveChannel()
        RtcEngine.destroy()
    }
}
