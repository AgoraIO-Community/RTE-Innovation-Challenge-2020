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

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.qifan.emojibattle.engine.GameEngine
import com.qifan.emojibattle.model.Emoji
import com.qifan.emojibattle.repository.GameRepository

class BattleViewModel(private val gameRepository: GameRepository) : ViewModel() {
  val emoji: LiveData<Emoji> = gameRepository.gameEmoji
  val gameStatus: LiveData<GameEngine.GameState> = gameRepository.gameStatus

  fun initialize() {
    gameRepository.initialize()
  }

  fun startGame(roomId: String, userId: String) {
    gameRepository.startGame(roomId, userId)
  }

  fun endGame(roomId: String) {
    gameRepository.endGame(roomId)
  }

  fun destory() {
    gameRepository.destory()
  }
}
