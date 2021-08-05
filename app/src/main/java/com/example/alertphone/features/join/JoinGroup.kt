package com.example.alertphone.features.join

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.example.alertphone.R
import com.example.alertphone.databinding.ActivityJoinGroupBinding
import com.example.alertphone.features.alert.GroupAlert

class JoinGroup : AppCompatActivity() {
    private lateinit var binding: ActivityJoinGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@JoinGroup, R.layout.activity_join_group)

        binding.etCode.setOnEditorActionListener { textview, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND){
                joinGroup()
                false
            } else
                true
        }

        binding.btnConfirm.setOnClickListener { joinGroup() }

    }

    private fun joinGroup() {
        val code = binding.etCode.text.toString()
        if (code != ""){
            nextActivity(GroupAlert::class.java)
        } else{
            binding.etCode.text = null
            binding.etCode.setHintTextColor(resources.getColor(R.color.red_400))
            binding.etCode.hint = "enter your code"
        }
    }

    private fun <T> nextActivity(cls: Class<T>) {
        val intent = Intent(this@JoinGroup, cls)
        this@JoinGroup.startActivity(intent)
        this@JoinGroup.finish()
    }
}