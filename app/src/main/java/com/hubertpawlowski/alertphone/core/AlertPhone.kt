package com.hubertpawlowski.alertphone.core

import android.app.Application
import android.provider.SyncStateContract
import com.hubertpawlowski.alertphone.Constants
import java.util.*
import kotlin.random.Random.Default.nextLong

class AlertPhone : Application() {

    override fun onCreate() {
        super.onCreate()
        initUser()
    }

    private fun initUser() {
        val editor = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE).edit()
        editor.putLong(Constants.PREF_ID, UUID.randomUUID().mostSignificantBits)
        editor.apply()
    }
}