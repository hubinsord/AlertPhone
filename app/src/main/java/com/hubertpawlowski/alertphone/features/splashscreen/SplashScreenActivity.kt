package com.hubertpawlowski.alertphone.features.splashscreen

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.hubertpawlowski.alertphone.Constants
import com.hubertpawlowski.alertphone.R
import com.hubertpawlowski.alertphone.features.alert.MainActivity
import com.hubertpawlowski.alertphone.features.onboarding.OnboardingActivity

class SplashScreenActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        hasSeenMainActivity()
    }

    private fun hasSeenMainActivity() {
        val prefs = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
        val groupName = prefs.getString(Constants.PREF_GROUP_NAME, "")
        Handler(Looper.getMainLooper()).postDelayed({
            if (groupName != "") {
                val intent = MainActivity.newIntent(this)
                startActivity(intent)
                finish()
            } else {
                val intent = OnboardingActivity.newIntent(this)
                startActivity(intent)
                finish()
            }
        }, SPLASH_DISPLAY_LENGTH)
    }

    companion object {
        private const val SPLASH_DISPLAY_LENGTH = 1500L
    }
}
