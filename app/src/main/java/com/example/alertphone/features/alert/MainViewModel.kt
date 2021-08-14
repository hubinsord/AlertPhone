package com.example.alertphone.features.alert

import androidx.lifecycle.MutableLiveData

class MainViewModel(private val groupAlertSubscriber: GroupAlertSubscriber) {
    val groupNameLiveData: MutableLiveData<String> = MutableLiveData()
    val titleLiveData: MutableLiveData<String> = MutableLiveData()
    val messageLiveData: MutableLiveData<String> = MutableLiveData()

    fun subscribe() {
        groupAlertSubscriber.subscribeToGroup()
    }

    fun getGroupName() {
        val groupName = groupAlertSubscriber.getGroupName()
        groupNameLiveData.postValue(groupName)
    }

    fun updateTitle(title: String) {
        titleLiveData.postValue(title)
    }

    fun updateMessage(message: String) {
        messageLiveData.postValue(message)
    }
}