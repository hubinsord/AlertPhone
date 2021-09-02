package com.hubertpawlowski.alertphone.features.onboarding.username

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserNameViewModel(private val userNameStorage: UserNameStorage): ViewModel() {

    val userNameLiveData: MutableLiveData<String> = MutableLiveData()

    fun updateUserName(userName: String){
        userNameLiveData.value = userName
        storeUserName(userName)
    }
     fun storeUserName(userName:String){
         userNameStorage.setUserNameSharedPreferences(userName)
     }
}