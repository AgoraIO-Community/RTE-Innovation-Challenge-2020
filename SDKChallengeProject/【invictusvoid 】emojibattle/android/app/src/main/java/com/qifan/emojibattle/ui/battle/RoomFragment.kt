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

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.qifan.emojibattle.R
import com.qifan.emojibattle.databinding.FragmentRoomBinding
import com.qifan.emojibattle.ui.base.BaseFragment
import com.qifan.powerpermission.askPermissions
import com.qifan.powerpermission.data.hasAllGranted
import com.qifan.powerpermission.data.hasPermanentDenied
import com.qifan.powerpermission.rationale.createDialogRationale
import com.qifan.powerpermission.rationale.delegate.RationaleDelegate

class RoomFragment : BaseFragment<FragmentRoomBinding>() {
  override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentRoomBinding =
    FragmentRoomBinding::inflate
  private val editChannel get() = binding.editChannel
  private val btnBattle get() = binding.btnBattle
  private val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)

  private val dialogRationaleDelegate: RationaleDelegate by lazy {
    createDialogRationale(
      dialogTitle = R.string.permission_dialog_title,
      requiredPermissions = permissions.toList(),
      message = getString(R.string.permission_dialog_message)
    )
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    btnBattle.setOnClickListener { askPermissionToBattle() }
  }

  private fun askPermissionToBattle() {
    askPermissions(*permissions, rationaleDelegate = dialogRationaleDelegate) { result ->
      when {
        result.hasAllGranted() -> {
          val roomId = editChannel.text
          if (!roomId.isNullOrEmpty()) {
            findNavController().navigate(
              RoomFragmentDirections.actionRoomFragmentToBattleFragment(
                roomId.toString()
              )
            )
          } else {
            Toast.makeText(
              requireContext(),
              R.string.input_room_id,
              Toast.LENGTH_LONG
            ).show()
          }
        }
        result.hasPermanentDenied() -> Toast.makeText(
          requireContext(),
          R.string.permission_denied,
          Toast.LENGTH_LONG
        ).show()
      }
    }
  }
}
