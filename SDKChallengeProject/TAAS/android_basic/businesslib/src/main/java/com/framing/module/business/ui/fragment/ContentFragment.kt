package com.framing.module.business.ui.fragment

<<<<<<< HEAD
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.framing.commonlib.agora.AgoraLiviView
=======
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
import com.young.aac.base.MvvmBaseFragment
import com.framing.module.business.databinding.FragmentContentLayoutBinding
import com.framing.module.business.ui.viewmodel.ContentUIVM
import com.framing.module.business.data.viewmodel.ContentDataVM
import com.framing.module.business.BR
import com.framing.module.business.R
<<<<<<< HEAD
import io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER
import io.agora.rtc.IRtcEngineEventHandler

=======
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5
/**
 * Des
 * Created by VULCAN on 2020-08-25 11:43:48
 */
class ContentFragment :  MvvmBaseFragment<FragmentContentLayoutBinding, ContentUIVM,ContentDataVM>(){

<<<<<<< HEAD
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBinding().liveView.startPrepare(CLIENT_ROLE_BROADCASTER,object :AgoraLiviView.IRtcStats{
            override fun statusResult(stats: IRtcEngineEventHandler.RtcStats?) {
            }
        })
    }
=======
>>>>>>> 2c9bdf6c20703b9f2aab594ec9adac6c013f62b5

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