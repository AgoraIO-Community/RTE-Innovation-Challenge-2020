package com.young.bean

import com.framing.module.customer.data.bean.NutrientBean
/*
* 面向UI 自己写接口感觉很爽啊
* */
data class PageAllBean
(var isShowDialog:Boolean,
 var dialogLevel:Int,//1 warning 2 attention 3 focus
 var focus:ContentListBean,
 var science:ContentListBean,
 var dialogBean: DialogBean,
 var bottom:BottomBean)

data class DialogBean(var isShowDialog:Boolean,var dialogLevel:Int,var subtitle:String,var content:String,var nutrientBean: NutrientBean)
data class BottomBean(var list: List<ContentListBean> )
data class ContentListBean(var title:String,var imgurl:String,var content:String,var contenturl:String,var nutrientBean: NutrientBean)
