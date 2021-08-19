package com.example.alertphone.features.alert

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(
    private val groupAlertSubscriber: GroupAlertSubscriber,
    private val alertSender: AlertSender,
    ) : ViewModel() {

    val groupNameLiveData: MutableLiveData<String> = MutableLiveData(groupAlertSubscriber.getGroupName())
    val stateLiveData: MutableLiveData<MainViewState> = MutableLiveData()
    private val messageLiveData: MutableLiveData<String> = MutableLiveData()

    fun subscribeForAlerts() {
        groupAlertSubscriber.subscribeToGroup()
    }

    private fun sendAlert() {
        alertSender.send(MainActivity.TITLE_MEDICAl, messageLiveData.value ?: "", groupNameLiveData.value ?: "")
    }

    fun updateState(state: MainViewState) {
        stateLiveData.postValue(state)
    }

    fun proceedWithAlert() {
        when (stateLiveData.value) {
            MainViewState.STANDBY -> {
                sendAlert()
                stateLiveData.postValue(MainViewState.ALERT_SENT)
            }
            MainViewState.ALERT_RECEIVED -> stateLiveData.postValue(MainViewState.STANDBY)
            MainViewState.ALERT_SENT -> return
        }
    }

    fun updateMessage(message: String) {
        messageLiveData.postValue(message)
    }
}