package com.example.alertphone.features.alert

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.alertphone.R
import com.example.alertphone.databinding.ActivityGroupAlertBinding

class GroupAlert: AppCompatActivity() {

    private lateinit var binding: ActivityGroupAlertBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@GroupAlert, R.layout.activity_group_alert)

    }
}