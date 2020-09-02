package com.qifan.emojibattle.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.qifan.emojibattle.R
import com.qifan.emojibattle.databinding.ActivityResultBinding
import com.qifan.emojibattle.databinding.ActivitySplashBinding
import com.qifan.emojibattle.view.TileDrawable

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val scrollBackground get() = binding.scrollingBackground
    private val play get() = binding.play
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        scrollBackground.setImageDrawable(
            AppCompatResources.getDrawable(this, R.drawable.pattern)?.let {
                TileDrawable(
                    it
                )
            })
        play.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}