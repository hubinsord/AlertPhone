package com.hubertpawlowski.alertphone.features.onboarding.groupcode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GroupCodeViewModel:ViewModel() {

    val groupCodeLiveData: MutableLiveData<String> = MutableLiveData()

    fun updateGroupCode(groupCode:String){
        groupCodeLiveData.value = groupCode
    }
}