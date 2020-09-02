package com.qifan.emojibattle.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.qifan.emojibattle.R
import com.qifan.emojibattle.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val result get() = binding.result
    private var isWinner: Boolean = false

    companion object {
        private const val WINNER = "winner"
        fun Activity.startResultActivity(win: Boolean) {
            startActivity(
                Intent(this, ResultActivity::class.java)
                    .putExtra(WINNER, win)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        parseIntents()
        result.text = if (isWinner) "WIN" else "LOSE"
    }

    private fun parseIntents() {
        val isWinner = intent.getBooleanExtra(WINNER, false)
        this.isWinner = isWinner
    }
}