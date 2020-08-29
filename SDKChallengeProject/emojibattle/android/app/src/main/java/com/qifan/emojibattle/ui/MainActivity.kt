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
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.qifan.emojibattle.BattleActivity.Companion.startBattleActivity
import com.qifan.emojibattle.databinding.ActivityMainBinding
import com.qifan.emojibattle.extension.debug
import com.qifan.powerpermission.askPermissions
import com.qifan.powerpermission.data.hasAllGranted
import com.qifan.powerpermission.data.hasPermanentDenied
import com.qifan.powerpermission.rationale.createDialogRationale
import com.qifan.powerpermission.rationale.delegate.RationaleDelegate

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val editChannel get() = binding.editChannel
    private val btnBattle get() = binding.btnBattle
    private val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    private val DEFAULT_CHANNEL = "demoChannel1"

    private val dialogRationaleDelegate: RationaleDelegate by lazy {
        createDialogRationale(
            dialogTitle = R.string.permission_dialog_title,
            requiredPermissions = permissions.toList(),
            message = getString(R.string.permission_dialog_message)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        editChannel.setText(DEFAULT_CHANNEL)
        btnBattle.setOnClickListener { askPermissionToBattle() }
    }

    private fun askPermissionToBattle() {
        askPermissions(*permissions) { result ->
            when {
                result.hasAllGranted() -> startBattleActivity(editChannel.text.toString())
                result.hasPermanentDenied() -> debug("need to do something here")
            }
        }
    }
}
