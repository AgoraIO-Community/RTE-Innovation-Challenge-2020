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
package com.qifan.emojibattle.engine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.qifan.emojibattle.R
import com.qifan.emojibattle.data.DataStore
import com.qifan.emojibattle.model.Emoji
import com.qifan.emojibattle.model.FrameFace
import com.qifan.emojibattle.model.FrameFace.*
import com.qifan.emojibattle.model.GameSessionResult
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.fixedRateTimer

private const val TIME_PERIOD_DURATION = 5000L

class GameEngine(private val dataStore: DataStore) {
  private val emojis = listOf(
    Emoji(R.drawable.smile, Emoji.State.SMILE),
    Emoji(R.drawable.leftwink, Emoji.State.LEFT_WINK),
    Emoji(R.drawable.rightwink, Emoji.State.RIGHT_WINK)
  )
  private var currentEmoji = emojis[0]
  private lateinit var timer: Timer
  private val _emoji: MutableLiveData<Emoji> = MutableLiveData()
  internal val emoji: LiveData<Emoji> = _emoji

  private val _gameStatus: MutableLiveData<GameState> = MutableLiveData(GameState.IDLE)
  internal val gameStatus: LiveData<GameState> = _gameStatus
  internal val results = hashSetOf<Emoji>()
  private val count: AtomicInteger = AtomicInteger(0)

  fun startGame(roomId: String, userId: String) {
    _gameStatus.postValue(GameState.GAMING)
    timer = fixedRateTimer("startGame", period = TIME_PERIOD_DURATION) {
      if (emojis.size - 1 < count.get()) {
        evaluate(roomId, userId)
        _gameStatus.postValue(GameState.END)
        timer.cancel()
      } else {
        currentEmoji = emojis[count.getAndIncrement()]
        _emoji.postValue(currentEmoji)
      }
    }
  }

  fun setResult(frameFace: FrameFace) {
    val state = when (frameFace) {
      SMILE -> Emoji.State.SMILE
      LEFT_WINK -> Emoji.State.LEFT_WINK
      RIGHT_WINK -> Emoji.State.RIGHT_WINK
      UNKNOWN -> Emoji.State.UNKNOWN
    }
    val target = currentEmoji
    if (target.verified) return
    if (target.state == state) {
      target.verified = true
    }
    results.add(target)
  }

  fun endGame(roomId: String) {
    _gameStatus.postValue(GameState.IDLE)
    results.clear()
    count.set(0)
    timer.cancel()
    dataStore.delete(roomId)
  }

  private fun evaluate(roomId: String, userId: String) {
    dataStore.setResult(GameSessionResult(roomId, userId, results.map { it.verified }.size)) {
      getResult(roomId, userId)
    }
  }

  private fun getResult(roomId: String, userId: String) {
    dataStore.getWinner(
      GameSessionResult(
        roomId,
        userId,
        results.map { it.verified }.size
      )
    ) { result ->
      if (result) {
        _gameStatus.postValue(GameState.WIN)
      } else {
        _gameStatus.postValue(GameState.LOSE)
      }
    }
  }

  enum class GameState {
    IDLE,
    GAMING,
    END,
    WIN,
    LOSE
  }
}
