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

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.qifan.emojibattle.databinding.ActivityMainBinding
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.video.VideoCanvas
import io.agora.rtc2.video.VideoEncoderConfiguration

private const val PERMISSION_REQ_ID_RECORD_AUDIO = 1
private const val PERMISSION_REQ_ID_CAMERA = 2

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val localSurfaceView get() = binding.localSurfaceView
    private val remoteSurfaceView get() = binding.remoteSurfaceView
    private val token = BuildConfig.Token
    private val appId = BuildConfig.AppId
    private lateinit var rtcEngine: RtcEngine
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (
            checkSelfPermission(
                Manifest.permission.RECORD_AUDIO,
                PERMISSION_REQ_ID_RECORD_AUDIO
            ) && checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA)
        ) {
            initAgoraEngineAndJoinChannel()
        }
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
                    override fun onJoinChannelSuccess(channel: String?, userId: Int, elapsed: Int) {
                        Log.d(
                            "Qifan",
                            """
                            join channel success channel:$channel
                            userId = $userId
                            elapsed = $elapsed
                            """.trimIndent()
                        )

                    }

                    override fun onFirstLocalVideoFrame(width: Int, height: Int, elapsed: Int) {
                        super.onFirstLocalVideoFrame(width, height, elapsed)
                        Log.d("Qifan", "onFirstLocalVideoFrame")
                    }

                    override fun onFirstRemoteVideoDecoded(
                        userId: Int,
                        width: Int,
                        height: Int,
                        elapsed: Int
                    ) {
                        Log.d("Qifan", "onFirstRemoteVideoDecoded")
                        super.onFirstRemoteVideoDecoded(userId, width, height, elapsed)
                        runOnUiThread { setRemoteVideo(userId) }
                    }
                }
            )
        } catch (e: Exception) {
            throw RuntimeException(
                "NEED TO check rtc sdk init fatal error ${Log.getStackTraceString(e)}"
            )
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
        rtcEngine.joinChannel(token, "demoChannel1", 0, channelMediaOptions)
        rtcEngine.startPreview()
//        rtcEngine.joinChannel(token, "demoChannel1", "Extra Optional Data", 0)
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

    private fun leaveChannel() {
        rtcEngine.leaveChannel()
    }


    private fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission),
                requestCode
            )
            return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        rtcEngine.stopPreview()
        leaveChannel()
        RtcEngine.destroy()
    }
}
