package com.hubertpawlowski.alertphone.features.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.hubertpawlowski.alertphone.R
import com.hubertpawlowski.alertphone.databinding.ActivityOnboardingBinding
import com.hubertpawlowski.alertphone.features.alert.*
import com.hubertpawlowski.alertphone.features.onboarding.groupcode.GroupCodeFragment
import com.hubertpawlowski.alertphone.features.onboarding.username.UserNameFragment

class OnboardingActivity : AppCompatActivity(), UserNameFragment.Companion.UserNameFragmentListener,
    GroupCodeFragment.Companion.GroupCodeFragmentListener {
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var viewModel: OnboardingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this@OnboardingActivity, R.layout.activity_onboarding)
        initViewModel()
        setInitialFragment()
        binding.btnConfirm.setOnClickListener { buttonConfirmClicked() }
    }

    private fun buttonConfirmClicked() {
        when (supportFragmentManager.findFragmentById(R.id.fragment_container_view)) {
            is UserNameFragment -> {
                userNameToGroupNameTransition()
                resetBtnConfirm()
            }
            is GroupCodeFragment -> openMainActivity()
        }
    }

    private fun resetBtnConfirm() {
        binding.inputText = ""
    }

    private fun userNameToGroupNameTransition() {
        val fragment = supportFragmentManager.findFragmentByTag(GroupCodeFragment::class.java.name)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container_view,
                fragment ?: GroupCodeFragment.newInstance(),
                GroupCodeFragment::class.java.name)
            commit()
        }
    }

    private fun setInitialFragment() {
        val fragment = supportFragmentManager.findFragmentByTag(UserNameFragment::class.java.name)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container_view,
                fragment ?: UserNameFragment.newInstance(),
                UserNameFragment::class.java.name)
            commit()
        }
    }

    private fun initViewModel() {
        val topicStorage = TopicStorage(this)
        val viewModelFactory = OnBoardingViewModelFactory(topicStorage)
        viewModel = ViewModelProvider(this, viewModelFactory).get(OnboardingViewModel::class.java)
    }

    override fun userNameChanged(userName: String) {
        binding.inputText = userName
    }

    override fun groupCodeChanged(groupCode: String) {
        binding.inputText = groupCode
        viewModel.updateGroupName(groupCode)
    }

    private fun openMainActivity() {
        val intent = MainActivity.newIntent(this)
        startActivity(intent)
        finish()
    }

    companion object {

        @JvmStatic
        fun newIntent(context: Context?): Intent {
            return Intent(context, OnboardingActivity::class.java)
        }
    }

}