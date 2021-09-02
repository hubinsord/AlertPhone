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
    private var user: String? = ""

    fun subscribeForAlerts() {
        groupAlertSubscriber.subscribeToGroup()
    }

    fun updateTitle(title: String) {
        titleLiveData.value = title
    }

    fun updateState(state: MainViewState) {
        stateLiveData.postValue(state)
    }

    fun updateMessage(message: String) {
        messageLiveData.value = message
    }

    private fun sendAlert() {
        val messageCombined = "$user: ${messageLiveData.value ?: ""}"
        alertSender.send(titleLiveData.value ?: "",
            messageCombined,
            groupNameLiveData.value ?: "",
            user ?: "")
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
        Handler(Looper.getMainLooper()).postDelayed({ stateLiveData.value = viewState }, delay)
    }

    fun setUser(userName: String?) {
        user = userName
    }


}