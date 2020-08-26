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
import android.graphics.Bitmap
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.qifan.emojibattle.databinding.ActivityBattleBinding
import com.qifan.emojibattle.extension.debug
import com.qifan.emojibattle.sdk.VideoRawData
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration
import kotlin.properties.Delegates.notNull

class BattleActivity : AppCompatActivity(), VideoRawData.VideoObserver {
    private lateinit var binding: ActivityBattleBinding
    private val localSurfaceView get() = binding.localSurfaceView
    private val remoteSurfaceView get() = binding.remoteSurfaceView

    private var channel: String by notNull()
    private val token = BuildConfig.Token
    private val appId = BuildConfig.AppId
    private lateinit var rtcEngine: RtcEngine

    private lateinit var detector: FaceDetector

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
        setContentView(binding.root)
        parseIntents()
        initAgoraEngineAndJoinChannel()
        initVideoRawData()
        initFaceML()
    }

    private fun parseIntents() {
        val contentChannel = intent.getStringExtra(CHANNEL)
        requireNotNull(contentChannel)
        channel = contentChannel
    }

    private fun initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine()
        setupLocalVideo()
        setupVideoProfile()
        joinChannel()
    }

    private fun initVideoRawData() {
        VideoRawData.instance.subscribe(this)
    }

    private fun initFaceML() {
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()

        detector = FaceDetection.getClient(highAccuracyOpts)
    }

    private fun initializeAgoraEngine() {
        try {
            rtcEngine = RtcEngine.create(applicationContext, appId, RtcEngineEventHandler())
        } catch (e: Exception) {
            error(e.stackTraceToString())
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
        surfaceView.setZOrderMediaOverlay(true)
        if (localSurfaceView.childCount > 0) {
            localSurfaceView.removeAllViews()
        }
        localSurfaceView.addView(
            surfaceView,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        val videoCanvas = VideoCanvas(
            surfaceView,
            VideoCanvas.RENDER_MODE_FIT,
            0
        )
        rtcEngine.setupLocalVideo(videoCanvas)
    }

    private fun joinChannel() {
        // if you do not specify the uid, Agora will assign one.
        rtcEngine.joinChannel(token, channel, "test", 0)
        rtcEngine.startPreview()
    }

    private fun setRemoteVideo(uid: Int) {
        if (remoteSurfaceView.childCount >= 1) {
            remoteSurfaceView.removeAllViews()
        }
        val surfaceView = RtcEngine.CreateRendererView(applicationContext)
        remoteSurfaceView.addView(
            surfaceView,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        surfaceView.setZOrderMediaOverlay(true)
        rtcEngine.setupRemoteVideo(
            VideoCanvas(
                surfaceView,
                VideoCanvas.RENDER_MODE_FIT,
                uid
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        rtcEngine.leaveChannel()
        rtcEngine.stopPreview()
        VideoRawData.instance.unsubscribe()
        RtcEngine.destroy()
    }

    inner class RtcEngineEventHandler : IRtcEngineEventHandler() {
        override fun onUserJoined(userId: Int, elapsed: Int) {
            super.onUserJoined(userId, elapsed)
            debug("onUserJoined $userId")
            runOnUiThread {
                setRemoteVideo(userId)
            }
        }

        override fun onUserOffline(userId: Int, reason: Int) {
            super.onUserOffline(userId, reason)
            debug("onUserOffline $reason")
            runOnUiThread {
                rtcEngine.setupRemoteVideo(
                    VideoCanvas(
                        null,
                        VideoCanvas.RENDER_MODE_FIT,
                        userId
                    )
                )
            }
        }
    }

    override fun onCaptureVideoFrame(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        detector.process(image).addOnSuccessListener { faces ->
            faces.forEach { debug("VisionImage $it") }
        }
    }
}
