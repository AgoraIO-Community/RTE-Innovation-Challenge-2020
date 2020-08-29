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
import com.qifan.emojibattle.model.Emoji
import com.qifan.emojibattle.model.FrameFace
import com.qifan.emojibattle.model.FrameFace.* // ktlint-disable no-wildcard-imports
import java.util.Timer
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.fixedRateTimer

private const val TIME_PERIOD_DURATION = 5000L

class GameEngine {
    private val emojis = listOf(
        Emoji(R.drawable.smile, Emoji.State.SMILE),
        Emoji(R.drawable.leftwink, Emoji.State.LEFT_WINK),
        Emoji(R.drawable.rightwink, Emoji.State.RIGHT_WINK)
    )
    private lateinit var timer: Timer
    private val _emoji: MutableLiveData<Emoji> = MutableLiveData()
    internal val emoji: LiveData<Emoji> = _emoji

    private val _gameStatus: MutableLiveData<GameState> = MutableLiveData(GameState.START)
    internal val gameStatus: LiveData<GameState> = _gameStatus
    private val results = mutableListOf<Boolean>()
    private val _result: MutableLiveData<List<Boolean>> = MutableLiveData(results)
    val result: LiveData<List<Boolean>> = _result
    private val count: AtomicInteger = AtomicInteger(0)

    fun startGame() {
        _gameStatus.postValue(GameState.GAMING)
        timer = fixedRateTimer("startGame", period = TIME_PERIOD_DURATION) {
            if (emojis.count() - 1 == count.get()) {
                _gameStatus.postValue(GameState.END)
            } else {
                _emoji.postValue(emojis[count.getAndIncrement()])
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
        val target = emojis[count.get()]
        if (target.verified) return
        if (target.state == state) {
            target.verified = true
            results.add(true)
            _result.postValue(results)
        } else {
            results.add(false)
        }
    }

    fun endGame() {
        count.set(0)
        timer.cancel()
    }

    enum class GameState {
        START,
        GAMING,
        END
    }
}
