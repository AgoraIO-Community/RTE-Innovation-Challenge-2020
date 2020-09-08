package com.framing.module.customer.ui.adapter

import android.content.Context
import com.framing.commonlib.base.BaseBindingAdapter
import com.framing.commonlib.base.IBindingClick
import com.framing.commonlib.base.IViewBindingClick
import com.framing.module.customer.R
import com.framing.module.customer.data.RecordBean
import com.framing.module.customer.databinding.AdapterRecordItemLayoutBinding

/*
 * Des  
 * Author Young
 * Date 
 */class RecordListAdapter :BaseBindingAdapter<AdapterRecordItemLayoutBinding,RecordBean> {
    private  var  click: IBindingClick<Any>
    constructor(context: Context,click: IBindingClick<Any>) : super(context, R.layout.adapter_record_item_layout){
        this.click=click
    }

    override fun bindView(viewHolder: CommonViewHolder?, position: Int) {
        viewHolder?.bindView?.data=getDataList()[position]
        viewHolder?.bindView?.click=click
    }

}