package com.hubertpawlowski.alertphone.features.alert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException


class MainViewModelFactory(
    private val subscriber: GroupAlertSubscriber,
    private val alertSender: AlertSender,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(subscriber, alertSender) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}