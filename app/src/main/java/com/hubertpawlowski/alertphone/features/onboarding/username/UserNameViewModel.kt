package com.hubertpawlowski.alertphone.features.onboarding.username

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserNameViewModel: ViewModel() {

    val userNameLiveData: MutableLiveData<String> = MutableLiveData()

    fun updateUserName(userName: String){
        userNameLiveData.value = userName
    }
}