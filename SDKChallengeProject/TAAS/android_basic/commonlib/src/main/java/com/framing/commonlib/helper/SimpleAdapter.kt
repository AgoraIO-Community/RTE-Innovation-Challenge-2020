package com.framing.commonlib.helper

import android.content.Context
import com.framing.commonlib.R
import com.framing.commonlib.base.BaseBindingAdapter
import com.framing.commonlib.databinding.SimpleItemLayoutBinding

/*
 * Des  
 * Author Young
 * Date 
 */class SimpleAdapter :BaseBindingAdapter<SimpleItemLayoutBinding,String> {
    constructor(context: Context) : super(context, R.layout.simple_item_layout)

    override fun bindView(viewHolder: CommonViewHolder?, position: Int) {
        viewHolder?.bindView?.data=getDataList()[position]
    }
}