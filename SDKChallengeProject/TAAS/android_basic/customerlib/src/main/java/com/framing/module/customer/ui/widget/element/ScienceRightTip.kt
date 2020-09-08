package com.framing.module.customer.ui.widget.element

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.framing.baselib.TLog
import com.framing.commonlib.base.IBindingClickEvent
import com.framing.commonlib.helper.SimpleAdapter
import com.framing.module.customer.R
import com.framing.module.customer.data.bean.WeatherLiveData
import com.young.bean.ContentListBean
import kotlinx.android.synthetic.main.scicence_content_parent_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
 * Des  
 * Author Young
 * Date 
 */class ScienceRightTip : ScienceContentView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
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


    private var titleName:String?=null//标题名
    private var contentListBean:ContentListBean?=null//业务数据 ui展示

    override fun initView(attrs: AttributeSet?) {
        super.initView(attrs)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScienceRightTip)
        titleName = typedArray.getString(R.styleable.ScienceRightTip_title_name)
        binding?.titleTv?.run {
            this.gravity=Gravity.LEFT
            this.gravity=Gravity.CENTER_VERTICAL
            text=titleName
        }
        isClickable=true
    }
    /*
   * 根据外层执行进度
   * */
    override fun progress(progress:Float){
        titleLogic(progress)
        binding?.constraintLayout?.run {
            setProgress(progress)
        }
    }
    fun complete(isComplete:Boolean){
        TLog.log("right_tip_complete","$isComplete  --- ${binding?.constraintLayout?.currentState}")
        if(isComplete){
//            if(binding?.constraintLayout?.currentState==R.id.start) {
                binding?.constraintLayout?.run {
                    setTransition(R.id.to_show)
                    transitionToEnd()
                    setProgress(1f)
                }
//            }else{
//                binding?.constraintLayout?.run {
//                    setTransition(R.id.to_show)
//                    transitionToStart()
//                }
//            }
        }
    }
    /*
    * 处理title内容 贴边居右 居中居中
    * */
    override fun titleLogic(progress:Float){
        if(progress>=1f) {
            binding?.titleTv?.gravity = Gravity.CENTER
        }else if(progress<=0.1f){
            binding?.titleTv?.gravity = Gravity.LEFT
            binding?.titleTv?.gravity = Gravity.CENTER_VERTICAL
        }
    }
    //一般业务数据处理 区别于weather
    fun setSimpleData(data:ContentListBean){
        contentListBean = data
        dataToUi()
    }
    //weather 数据构造 把WeatherLiveData转换为weather Ui数据构造
    fun setWeatherData(data:WeatherLiveData){
        TLog.log("setWeatherData","$data")
        binding?.confirmBtn?.run {
            visibility=View.VISIBLE
            text=resources.getText(R.string.app_customer_load_more)
        }
        binding?.contentView?.contentBinding()?.run {
            weatherView?.visibility=View.VISIBLE
            recycleview?.visibility=View.VISIBLE
        }
        val weatherList= arrayListOf<String>()
        viewScope?.launch {
            withContext(Dispatchers.IO){
                for(i in 3 downTo 1){
                    when(i){
                        3->{
                            weatherList.add(data.weather)
                        }
                        2->{
                            weatherList.add(data.wind)
                        }
                        1->{
                            weatherList.add(data.humidity)
                        }
                    }
                }
            }
            withContext(Dispatchers.Main){
                val adapter=SimpleAdapter(context)
                val manager=LinearLayoutManager(context)
                manager.orientation=LinearLayoutManager.VERTICAL
                binding?.contentView?.contentBinding()?.recycleview?.layoutManager=manager
                binding?.contentView?.contentBinding()?.recycleview?.adapter=adapter
                binding?.contentView?.contentBinding()?.weatherView?.weatherStyle(weatherList[0])
                adapter.dataList=weatherList
                TLog.log("setWeatherData2","$weatherList")
            }
        }
        binding?.confirmBtn?.setOnClickListener {
            Toast.makeText(context,"模拟天气对植物增产驱动",Toast.LENGTH_SHORT).show()
        }
    }
    private fun dataToUi(){
        contentListBean?.run {
            binding?.contentView?.contentBinding()?.run {
                if(title.isEmpty()){
                    leftTipTv.visibility= View.GONE
                }else{
                    leftTipTv.visibility= View.VISIBLE
                    leftTipTv.text="$title :"
                }
                if(content.isEmpty()){
                    bottomTv.visibility= View.GONE
                }else{
                    bottomTv.visibility= View.VISIBLE
                    bottomTv.text=content
                }
                if(nutrientBean!=null){
                    percentView.visibility= View.VISIBLE
                    percentView.viewType(DataPercentView.ViewType.CYLINDER)
                }else{
                    percentView.visibility= View.GONE
                }
            }
        }
    }

    override fun clickLisen(click: IBindingClickEvent<Any>) {
        super.clickLisen(click)
        binding?.confirmBtn?.setOnClickListener {
            click.onClick(it,titleName!!,2)
        }
        binding?.contentView?.contentBinding()?.percentView?.setOnClickListener {
            click.onClick(it,titleName!!,2)
        }
    }
}