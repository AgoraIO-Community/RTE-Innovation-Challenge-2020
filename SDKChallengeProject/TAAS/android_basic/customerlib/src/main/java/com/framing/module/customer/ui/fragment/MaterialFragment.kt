package com.framing.module.customer.ui.fragment

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.Observer
import com.framing.commonlib.widget.StatusLayout
import com.young.aac.base.MvvmBaseFragment
import com.framing.module.customer.databinding.FragmentMaterialLayoutBinding
import com.framing.module.customer.ui.viewmodel.MaterialUIVM
import com.framing.module.customer.data.viewmodel.MaterialDataVM
import com.framing.module.customer.BR
import com.framing.module.customer.R
import com.framing.module.customer.ui.viewmodel.CContainerUIVM
import com.framing.module.customer.ui.widget.element.DataPercentView

/**
 * Des
 * Created by VULCAN on 2020-09-05 13:30:33
 */
class MaterialFragment :  MvvmBaseFragment<FragmentMaterialLayoutBinding, MaterialUIVM,MaterialDataVM>(){

    private var containerUIVM:CContainerUIVM?=null// activity UIvm (相当于application)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBinding().percentView.viewType(DataPercentView.ViewType.CYLINDER)
        containerUIVM=getActivityViewModelProvider(requireActivity()).get(CContainerUIVM::class.java)
        containerUIVM?.isConainerBgShow?.value=true
        getDataViewModel()?.requestData()
        getBinding()?.showText.setMovementMethod(ScrollingMovementMethod.getInstance());
        getBinding()?.constraintLayout4.addTransitionListener(object : MotionLayout.TransitionListener{
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
                    }
                }
            }

        })
        observerChange()
    }

    private fun observerChange() {
        getDataViewModel()?.showContent?.observe(viewLifecycleOwner, Observer {
            getUIViewModel()?.showContent.value=it
        })
    }

    override fun getUIViewModel(): MaterialUIVM {
           return getActivityViewModelProvider(requireActivity()).get(MaterialUIVM::class.java)
      }
      override fun getDataViewModel(): MaterialDataVM? {
           return getActivityViewModelProvider(requireActivity()).get(MaterialDataVM::class.java)!!
      }
      override val bindingVariable: Int
      get() = BR.materialUIVM
      override val layoutId: Int
      get() = R.layout.fragment_material_layout
}