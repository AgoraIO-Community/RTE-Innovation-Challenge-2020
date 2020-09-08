package com.framing.module.customer.ui.widget.element

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.framing.module.customer.R
import com.framing.module.customer.databinding.ContentStyleParentLayoutBinding

/*
 * Des  内容载体
 * Author Young
 * Date 
<<<<<<< HEAD
 */open class ContentParentView :FrameLayout {
=======
 */class ContentParentView :FrameLayout {
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
    constructor(context: Context) : super(context){
        initLayout()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        initLayout()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private var contentBinding:ContentStyleParentLayoutBinding?=null
    private fun initLayout(){
        contentBinding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.content_style_parent_layout,this,true);
    }
    fun contentBinding():ContentStyleParentLayoutBinding{
        return contentBinding!!
    }
}