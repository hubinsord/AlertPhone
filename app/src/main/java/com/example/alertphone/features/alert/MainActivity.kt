package com.example.alertphone.features.alert

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.transition.TransitionManager
import com.example.alertphone.R
import com.example.alertphone.data.main.api.NotificationApiInstance
import com.example.alertphone.data.main.model.NotificationData
import com.example.alertphone.data.main.model.PushNotification
import com.example.alertphone.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var isCalling = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        val groupAlertSubscriber = GroupAlertSubscriber(this)
        viewModel = MainViewModel(groupAlertSubscriber)
        viewModel.getGroupName()
        viewModel.subscribe()
        isCalling = intent.getBooleanExtra(EXTRA_CALLING, false)

        if (isCalling) {
            hideMainContainer()
        }

        viewModel.groupNameLiveData.observe(this, Observer { groupName ->
            binding.groupName = groupName
            binding.ivEmergencyCall.setOnClickListener {
                if (isCalling) {
                    showMainContainer()
                } else {
                    val title = binding.etTitle.text.toString()
                    val message = binding.etMessage.text.toString()
                    if (title.isNotEmpty() && message.isNotEmpty()) {
                        val pushNotification = createPushNotification(title, message, groupName)
                        sendNotification(pushNotification)
                    }
                }
            }
        })
    }

    private fun hideMainContainer() {
        binding.mainContainer.isVisible = false
    }

    private fun showMainContainer() {
        TransitionManager.beginDelayedTransition(binding.clMain)
        binding.mainContainer.isVisible = true
        isCalling = false
    }

    private fun createPushNotification(
        title: String,
        message: String,
        groupName: String?,
    ): PushNotification {
        val notificationData = NotificationData(title, message)
        return PushNotification(notificationData, "/topics/$groupName")
    }

    private fun sendNotification(pushNotification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = NotificationApiInstance.api.postNotification(pushNotification)
                if (response.isSuccessful)
                    Log.d("TAG", "Response successful}")
                else
                    Log.e("TAG", response.errorBody().toString())
            } catch (e: Exception) {
                Log.e("TAG", e.toString())
            }
        }

    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result         // Gets new FCM registration token
            val msg = getString(R.string.msg_token_fmt, token)      // Log and toast
            Log.d("Refreshed token: ", msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
    }

    companion object {
        private const val EXTRA_CALLING = "extra-calling"

        @JvmStatic
        fun newIntent(context: Context?, isCalling: Boolean?): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(EXTRA_CALLING, isCalling)
            return intent
        }

        @JvmStatic
        fun newIntent(context: Context?): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
