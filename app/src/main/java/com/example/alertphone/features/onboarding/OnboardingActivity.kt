package com.example.alertphone.features.onboarding

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import com.example.alertphone.R
import com.example.alertphone.databinding.ActivityOnboardingBinding
import com.example.alertphone.features.alert.MainActivity

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var viewModel: OnboardingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this@OnboardingActivity, R.layout.activity_onboarding)

        val topicStorage = TopicStorage(this)
        viewModel = OnboardingViewModel(topicStorage)

        binding.etGroupName.doAfterTextChanged {
            viewModel.updateGroupName(it.toString())
        }

        viewModel.groupNameLiveData.observe(this, { groupName ->
            binding.groupName = groupName
            binding.btnConfirm.setOnClickListener {
                joinGroup(groupName)
            }
        })
    }

    private fun setImeOption(view: TextView, action: Int, groupName: String) {
        view.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == action) {
                joinGroup(groupName)
                false
            } else
                true
        }
    }

    private fun joinGroup(topic: String) {
        if (topic != "") {
            val intent = MainActivity.newIntent(this)
            startActivity(intent)
            finish()
        } else {
            binding.etGroupName.text = null
            binding.etGroupName.setHintTextColor(resources.getColor(R.color.red_400))
            binding.etGroupName.hint = "enter your code"
        }
    }
}