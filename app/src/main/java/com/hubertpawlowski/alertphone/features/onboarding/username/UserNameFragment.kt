package com.hubertpawlowski.alertphone.features.onboarding.username

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hubertpawlowski.alertphone.databinding.FragmentUserNameBinding

class UserNameFragment : Fragment() {

    lateinit var viewModel:UserNameViewModel
    lateinit var binding: FragmentUserNameBinding
    lateinit var listener: UserNameFragmentListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = activity as UserNameFragmentListener
        } catch (castException: ClassCastException) {
            throw NotImplementedError("class cast failed")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUserNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(UserNameViewModel::class.java)

        binding.etGroupCode.doAfterTextChanged {
            viewModel.updateUserName(it.toString())
        }

        viewModel.userNameLiveData.observe(viewLifecycleOwner, {
            listener.userNameChanged(it.toString())
        })
    }

    companion object{
        @JvmStatic
        fun newInstance() = UserNameFragment()

        interface UserNameFragmentListener{
            fun userNameChanged(userName: String)
        }
    }
}