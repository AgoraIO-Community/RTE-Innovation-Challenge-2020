
package com.framing.commonlib.base

import android.view.View
/*
 * Des
 * Author Young
 * Date 2020-05-25
 */
interface IBindingClick<T> {
    fun onClick(data:T)
}
interface IViewBindingClick {
    fun onClick(view:View)
}
interface IBindingViewClick<T> {
    fun onClick(data:T,view:View)
}
interface IBindingItemClick<T> {
    fun onClick(data:T,position:Int)
}
interface IBindingClickA<D> {
    fun onClick(view: View,data:D,position:Int)
}