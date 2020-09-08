package com.framing.module.customer.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import com.framing.module.customer.BR
import com.framing.module.customer.R
import com.framing.module.customer.databinding.FragmentStartBinding
import com.framing.module.customer.ui.viewmodel.MapUIVM
import com.framing.module.customer.ui.viewmodel.StartFUiVM
import com.young.aac.base.BaseDataViewModel
import com.young.aac.base.MvvmBaseFragment
import java.util.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/*
* google 构建 look  look  就算了
* */
/**
 * A simple [Fragment] subclass.
 * Use the [StartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StartFragment : MvvmBaseFragment<FragmentStartBinding, StartFUiVM,BaseDataViewModel>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        getBinding().motionLayout.setTransition(R.id.start_transition)
        getBinding().motionLayout.transitionToState(R.id.start)
        getBinding().motionLayout.transitionToEnd()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override val bindingVariable: Int
        get() = BR.startVM
    override val layoutId: Int
        get() = R.layout.fragment_start

    override fun getUIViewModel(): StartFUiVM {
        return getFragmentViewModelProvider(this).get(StartFUiVM::class.java)
    }
    override fun getDataViewModel(): BaseDataViewModel? {
        return null
    }
}