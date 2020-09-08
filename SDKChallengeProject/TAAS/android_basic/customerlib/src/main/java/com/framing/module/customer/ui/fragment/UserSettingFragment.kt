package com.framing.module.customer.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import com.framing.commonlib.widget.StatusLayout
import com.young.aac.base.MvvmBaseFragment
import com.framing.module.customer.databinding.DispanseSettingLayoutBinding
import com.framing.module.customer.ui.viewmodel.UserSettingUIVM
import com.framing.module.customer.data.viewmodel.UserSettingDataVM
import com.framing.module.customer.BR
import com.framing.module.customer.R
import com.framing.module.customer.ui.viewmodel.CContainerUIVM

/**
 * Des
 * Created by VULCAN on 2020-08-28 14:43:34
 */
class UserSettingFragment :  MvvmBaseFragment<DispanseSettingLayoutBinding, UserSettingUIVM,UserSettingDataVM>(){

    private var containerUIVM:CContainerUIVM?=null// activity UIvm (相当于application)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        containerUIVM=getActivityViewModelProvider(requireActivity()).get(CContainerUIVM::class.java)
        getUIViewModel()?.loadType.value=(StatusLayout.StatusType.EMPTY)
        containerUIVM?.isConainerBgShow?.value=true
        getBinding()?.constraintLayout3.addTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                when(p1){
                    R.id.top_end->{
                        containerUIVM?.isConainerBgShow?.value=false
                        nav()?.navigateUp()
//                        nav()?.navigate(R.id.action_userSettingFragment_to_mapFragment)
                    }
                }
            }

        })
    }
      override fun getUIViewModel(): UserSettingUIVM {
           return getActivityViewModelProvider(requireActivity()).get(UserSettingUIVM::class.java)
      }
      override fun getDataViewModel(): UserSettingDataVM? {
           return getActivityViewModelProvider(requireActivity()).get(UserSettingDataVM::class.java)!!
      }
      override val bindingVariable: Int
      get() = BR.userSettingUIVM
      override val layoutId: Int
      get() = R.layout.dispanse_setting_layout
}