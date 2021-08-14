package com.example.alertphone.features.alert

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.alertphone.R
import com.example.alertphone.data.main.api.NotificationApiInstance
import com.example.alertphone.data.main.model.NotificationData
import com.example.alertphone.data.main.model.PushNotification
import com.example.alertphone.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        val groupAlertSubscriber = GroupAlertSubscriber(this)
        viewModel = MainViewModel(groupAlertSubscriber)
        viewModel.getGroupName()
        viewModel.subscribe()
        getToken()

        val title = binding.etTitle.text.toString()
        viewModel.updateTitle(title)

        val message = binding.etMessage.text.toString()
        viewModel.updateMessage(message)

        viewModel.groupNameLiveData.observe(this, Observer { groupName ->
            binding.groupName = groupName

            binding.ivEmergencyCall.setOnClickListener {
                if(title.isNotEmpty() && message.isNotEmpty()){
                    val notificationData = NotificationData(title, message)
                    val pushNotification = PushNotification(notificationData, groupName)
                    sendNotification(pushNotification)
                }
            }
        })
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
        fun newIntent(context: Context?, text: String?): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(EXTRA_CALLING, text)
            return intent
        }

        @JvmStatic
        fun newIntent(context: Context?): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
