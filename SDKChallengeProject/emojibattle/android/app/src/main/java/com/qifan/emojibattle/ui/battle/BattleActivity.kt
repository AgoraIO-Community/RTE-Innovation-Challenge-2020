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
package com.qifan.emojibattle.ui.battle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.qifan.emojibattle.BuildConfig
import com.qifan.emojibattle.R
import com.qifan.emojibattle.databinding.ActivityBattleBinding
import com.qifan.emojibattle.engine.GameEngine.GameState.*
import com.qifan.emojibattle.extension.debug
import com.qifan.emojibattle.ui.ResultActivity.Companion.startResultActivity
import com.squareup.picasso.Picasso
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates.notNull
import kotlin.random.Random

class BattleActivity : AppCompatActivity() {
  private lateinit var binding: ActivityBattleBinding
  private val localSurfaceView get() = binding.localSurfaceView
  private val remoteSurfaceView get() = binding.remoteSurfaceView
  private val engineEmoji get() = binding.engineEmoji
  private val loading get() = binding.loading
  private val localResult get() = binding.localResult
  private val remoteResult get() = binding.remoteResult

  private val viewModel: BattleViewModel by viewModel()

  private val localUserId: Int by lazy { Random.nextInt() }
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
    setContentView(binding.root)
    parseIntents()
    initAgoraEngineAndJoinChannel()
    initVideoRawData()
    observerModel()
  }

  private fun observerModel() {
    viewModel.emoji.observe(this) { emoji ->
      Picasso.get()
        .load(emoji.battleRes)
        .centerCrop()
        .resize(200, 200)
        .into(engineEmoji)
    }
    viewModel.gameStatus.observe(this) { state ->
      when (state) {
        IDLE -> {
          engineEmoji.visibility = View.GONE
          loading.visibility = View.GONE
        }
        GAMING -> {
          engineEmoji.visibility = View.VISIBLE
          loading.visibility = View.GONE
        }
        END -> loading.visibility = View.VISIBLE
        WIN -> {
          loading.visibility = View.GONE
          startResultActivity(true)
          finish()
        }
        LOSE -> {
          loading.visibility = View.GONE
          startResultActivity(false)
          finish()
        }
        else -> {
        }
      }
    }
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
    viewModel.initialize()
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
    rtcEngine.disableAudio()
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
    localSurfaceView.addView(surfaceView)
    val videoCanvas = VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0)
    rtcEngine.setupLocalVideo(videoCanvas)
  }

  private fun joinChannel() {
    // if you do not specify the uid, Agora will assign one.
    rtcEngine.joinChannel(token, channel, getString(R.string.app_name), localUserId)
    rtcEngine.startPreview()
  }

  private fun setRemoteVideo(uid: Int) {
    if (remoteSurfaceView.childCount >= 1) {
      remoteSurfaceView.removeAllViews()
    }
    val surfaceView = RtcEngine.CreateRendererView(applicationContext)
    remoteSurfaceView.addView(surfaceView)
    surfaceView.setZOrderMediaOverlay(true)
    rtcEngine.setupRemoteVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, uid))
  }

  override fun onDestroy() {
    rtcEngine.leaveChannel()
    rtcEngine.stopPreview()
    viewModel.emoji.removeObservers(this)
    viewModel.gameStatus.removeObservers(this)
    viewModel.destory()
    RtcEngine.destroy()
    super.onDestroy()
  }

  inner class RtcEngineEventHandler : IRtcEngineEventHandler() {
    override fun onUserJoined(userId: Int, elapsed: Int) {
      super.onUserJoined(userId, elapsed)
      debug("onUserJoined $userId")
      runOnUiThread {
        viewModel.startGame(channel, localUserId.toString())
        setRemoteVideo(userId)
      }
    }

    override fun onUserOffline(userId: Int, reason: Int) {
      super.onUserOffline(userId, reason)
      debug("onUserOffline $reason")
      runOnUiThread {
        viewModel.endGame(channel)
        rtcEngine.setupRemoteVideo(
          VideoCanvas(
            null,
            VideoCanvas.RENDER_MODE_HIDDEN,
            userId
          )
        )
      }
    }
  }
}
