package com.example.alertphone.features.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.alertphone.R
import com.example.alertphone.features.onboarding.OnboardingActivity

class SplashScreenActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler(Looper.getMainLooper()).postDelayed(
            Runnable {
                val intent = Intent(this@SplashScreenActivity, OnboardingActivity::class.java)
                this@SplashScreenActivity.startActivity(intent)
                this@SplashScreenActivity.finish()
            }, SPLASH_DISPLAY_LENGTH
        )
    }

    companion object {
        private const val SPLASH_DISPLAY_LENGTH = 1500L
    }
}
