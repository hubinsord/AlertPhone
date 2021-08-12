package com.example.alertphone.features.alert

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.alertphone.R
import com.example.alertphone.data.main.api.NotificationApiInstance
import com.example.alertphone.data.main.model.PushNotification
import com.example.alertphone.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

const val TAG = "PushNotification"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var topic: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
    }

    private fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = NotificationApiInstance.api.postNotification(notification)
                if (response.isSuccessful)
//                    Log.d(TAG, "Response: ${Gson().toJson(response)}")
                else
                    Log.e(TAG, response.errorBody().toString())
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }

    companion object {
        const val EXTRA_CALLING = "extra-calling"

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
