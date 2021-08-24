package com.hubertpawlowski.alertphone.features.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OnboardingViewModel(private val topicStorage: TopicStorage) : ViewModel() {

    val groupNameLiveData: MutableLiveData<String> = MutableLiveData()


    fun updateGroupName(groupName: String) {
        groupNameLiveData.postValue(groupName)
        storeGroupName(groupName)
    }

    private fun storeGroupName(topic: String) {
        topicStorage.setGroupNameSharedPreferences(topic)
    }
}