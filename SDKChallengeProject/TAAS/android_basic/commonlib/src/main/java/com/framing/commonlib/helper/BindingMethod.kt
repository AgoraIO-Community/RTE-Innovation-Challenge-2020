package com.framing.commonlib.helper

import android.view.View
import androidx.databinding.BindingAdapter
import com.framing.baselib.TLog
import com.framing.commonlib.base.IBindingClickEvent

class BindingMethod {
    companion object {
        @JvmStatic
        @BindingAdapter(value = ["visible"], requireAll = false)
        fun visible(view: View, visible: Boolean) {
            TLog.log("BindingAdapter", "$view+visible"+visible)
            view.visibility = if (visible) View.VISIBLE else View.GONE
        }
    }
}