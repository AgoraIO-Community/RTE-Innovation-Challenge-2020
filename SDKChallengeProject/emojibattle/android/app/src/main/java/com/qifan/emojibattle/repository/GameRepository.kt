package com.qifan.emojibattle.repository

import com.qifan.emojibattle.engine.GameEngine

class GameRepository(
    private val gameEngine: GameEngine
) {

    internal fun startGame() {
        gameEngine.startGame()
    }

    internal fun endGame() {

    }
}