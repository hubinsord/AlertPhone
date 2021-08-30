package com.hubertpawlowski.alertphone.features.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OnboardingViewModel(private val topicStorage: TopicStorage) : ViewModel() {

    val groupCodeLiveData: MutableLiveData<String> = MutableLiveData()


    fun updateGroupName(groupName: String) {
        groupCodeLiveData.postValue(groupName)
        storeGroupName(groupName)
    }

    private fun storeGroupName(topic: String) {
        topicStorage.setGroupNameSharedPreferences(topic)
    }
}