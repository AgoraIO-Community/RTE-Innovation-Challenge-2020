package com.framing.commonlib.helper

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.framing.baselib.TLog
import com.framing.commonlib.R
import com.framing.commonlib.base.IBindingClick
import com.framing.commonlib.base.IBindingClickEvent
import com.framing.commonlib.widget.StatusLayout

class BindingMethod {
    companion object {
        @JvmStatic
        @BindingAdapter(value = ["visible"], requireAll = false)
        fun visible(view: View, visible: Boolean) {
            TLog.log("BindingAdapter", "$view+visible"+visible)
            view.visibility = if (visible) View.VISIBLE else View.GONE
        }
        @JvmStatic
        @BindingAdapter(value = ["loadingStatus"], requireAll = false)
        fun loadingStatus(view: StatusLayout, status: StatusLayout.StatusType?) {
            TLog.log("loadingStatus", "$status")
            if(status!=null) {
                view?.statusType(status)
            }
        }
        @JvmStatic
        @BindingAdapter(value = ["imgLoad"], requireAll = false)
        fun imgLoad(view: ImageView, url:String) {
            TLog.log("BindingAdapter", "$view")
            view.load(url){
                crossfade(true)
                placeholder(R.mipmap.img_holder)
                transformations(RoundedCornersTransformation())
            }
        }
    }
}