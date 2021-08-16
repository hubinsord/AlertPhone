package com.example.alertphone.features.alert

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(private val groupAlertSubscriber: GroupAlertSubscriber): ViewModel() {
    val groupNameLiveData: MutableLiveData<String> = MutableLiveData()

    fun subscribe() {
        groupAlertSubscriber.subscribeToGroup()
    }

    fun getGroupName() {
        val groupName = groupAlertSubscriber.getGroupName()
        groupNameLiveData.postValue(groupName)
    }
}