package com.hubertpawlowski.alertphone.features.alert

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(
    private val groupAlertSubscriber: GroupAlertSubscriber,
    private val alertSender: AlertSender,
) : ViewModel() {

    val groupNameLiveData: MutableLiveData<String> =
        MutableLiveData(groupAlertSubscriber.getGroupName())
    val stateLiveData: MutableLiveData<MainViewState> = MutableLiveData()
    private val messageLiveData: MutableLiveData<String> = MutableLiveData()
    private val titleLiveData: MutableLiveData<String> = MutableLiveData()

    fun subscribeForAlerts() {
        groupAlertSubscriber.subscribeToGroup()
    }

    private fun sendAlert() {
        alertSender.send(titleLiveData.value ?: "",
            messageLiveData.value ?: "",
            groupNameLiveData.value ?: "")
    }

    fun updateState(state: MainViewState) {
        stateLiveData.postValue(state)
    }

    fun proceedWithAlert() {
        when (stateLiveData.value) {
            MainViewState.STANDBY -> {
                sendAlert()
                stateLiveData.postValue(MainViewState.ALERT_SENT)
                changeViewStateOverTime(MainViewState.STANDBY, 10000)
            }
            MainViewState.ALERT_RECEIVED -> stateLiveData.postValue(MainViewState.STANDBY)
            MainViewState.ALERT_SENT -> return
        }
    }

    private fun changeViewStateOverTime(viewState: MainViewState, delay: Long) {
        Handler(Looper.getMainLooper()).postDelayed(
            Runnable { stateLiveData.value = viewState  }, delay )
    }

    fun updateMessage(message: String) {
        messageLiveData.value = message
    }

    fun updateTitle(title: String) {
        titleLiveData.value = title
    }
}