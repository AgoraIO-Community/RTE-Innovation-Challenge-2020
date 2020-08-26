package com.framing.module.business.ui.fragment

import com.young.aac.base.MvvmBaseFragment
import com.framing.module.business.databinding.FragmentContentLayoutBinding
import com.framing.module.business.ui.viewmodel.ContentUIVM
import com.framing.module.business.data.viewmodel.ContentDataVM
import com.framing.module.business.BR
import com.framing.module.business.R
/**
 * Des
 * Created by VULCAN on 2020-08-25 11:43:48
 */
class ContentFragment :  MvvmBaseFragment<FragmentContentLayoutBinding, ContentUIVM,ContentDataVM>(){


      override fun getUIViewModel(): ContentUIVM {
           return getActivityViewModelProvider(requireActivity()).get(ContentUIVM::class.java)
      }
      override fun getDataViewModel(): ContentDataVM? {
           return getActivityViewModelProvider(requireActivity()).get(ContentDataVM::class.java)!!
      }
      override val bindingVariable: Int
      get() = BR.contentUIVM
      override val layoutId: Int
      get() = R.layout.fragment_content_layout
}