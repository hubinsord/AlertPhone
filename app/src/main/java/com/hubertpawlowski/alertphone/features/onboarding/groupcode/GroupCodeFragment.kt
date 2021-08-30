package com.hubertpawlowski.alertphone.features.onboarding.groupcode

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.hubertpawlowski.alertphone.databinding.FragmentGroupCodeBinding
import com.hubertpawlowski.alertphone.features.onboarding.OnboardingViewModel
import com.hubertpawlowski.alertphone.features.onboarding.username.UserNameFragment


class GroupCodeFragment : Fragment() {

    lateinit var binding: FragmentGroupCodeBinding
    lateinit var viewModel: GroupCodeViewModel
    lateinit var listener: GroupCodeFragmentListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = activity as GroupCodeFragmentListener
        } catch (castException: ClassCastException) {
            throw NotImplementedError("class cast failed")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGroupCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(GroupCodeViewModel::class.java)

        binding.etGroupCode.doAfterTextChanged {
            listener.groupCodeChanged(it.toString())
        }

        viewModel.groupCodeLiveData.observe(viewLifecycleOwner, {
            listener.groupCodeChanged(it.toString())
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = GroupCodeFragment()

        interface GroupCodeFragmentListener{
            fun groupCodeChanged(groupCode: String)
        }

    }
}
