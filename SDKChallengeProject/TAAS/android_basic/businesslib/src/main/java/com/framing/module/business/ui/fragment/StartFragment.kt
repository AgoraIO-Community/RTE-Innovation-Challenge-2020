package com.framing.module.business.ui.fragment

import android.os.Bundle
import android.view.View
import com.framing.baselib.TLog
import com.young.aac.base.MvvmBaseFragment
import com.framing.module.business.databinding.FragmentStartLayoutBinding
import com.framing.module.business.ui.viewmodel.StartUIVM
import com.framing.module.business.data.viewmodel.StartDataVM
import com.framing.module.business.BR
import com.framing.module.business.R
/**
 * Des
 * Created by VULCAN on 2020-08-25 11:43:22
 */
class StartFragment :  MvvmBaseFragment<FragmentStartLayoutBinding, StartUIVM,StartDataVM>(){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TLog.log("onviewcreate")
        getBinding().motionLayout.setTransition(R.id.start_transition)
        getBinding().motionLayout.transitionToState(R.id.start)
        getBinding().motionLayout.setProgress(0f)
        getBinding().motionLayout.transitionToEnd()
    }
      override fun getUIViewModel(): StartUIVM {
           return getActivityViewModelProvider(requireActivity()).get(StartUIVM::class.java)
      }
      override fun getDataViewModel(): StartDataVM? {
           return getActivityViewModelProvider(requireActivity()).get(StartDataVM::class.java)!!
      }
      override val bindingVariable: Int
      get() = BR.startUIVM
      override val layoutId: Int
      get() = R.layout.fragment_start_layout
}