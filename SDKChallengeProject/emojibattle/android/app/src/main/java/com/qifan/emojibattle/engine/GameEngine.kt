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

import com.google.mlkit.vision.face.Face
import com.qifan.emojibattle.R
import com.qifan.emojibattle.internal.Queue
import java.util.*
import kotlin.concurrent.fixedRateTimer


private const val TIME_PERIOD_DURATION = 5000L

class GameEngine {
    private val emojis = Queue<Pair<Int, EmojiState>>()
    private lateinit var timer: Timer
    private lateinit var listener: Listener

    init {
        emojis.push(R.drawable.smile to EmojiState.SMILE)
        emojis.push(R.drawable.leftwink to EmojiState.LEFT_WINK)
        emojis.push(R.drawable.rightwink to EmojiState.RIGHT_WINK)
    }

    fun startGame(listener: Listener) {
        this.listener = listener
        timer = fixedRateTimer("startGame", period = TIME_PERIOD_DURATION) {
            if (emojis.isNotEmpty()) {
                listener.displayEmoji(emojis.pop())
            } else {
                //开始评分
            }
        }
    }


    fun endGame() {
        timer.cancel()
    }

    interface Listener {
        fun displayEmoji(emoji: Pair<Int, EmojiState>)
        fun transformFaceToEmoji(face: Face)
    }

    enum class EmojiState {
        SMILE, LEFT_WINK, RIGHT_WINK, UNKNOWN
    }
}
