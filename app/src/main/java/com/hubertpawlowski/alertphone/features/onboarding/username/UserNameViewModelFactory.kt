package com.hubertpawlowski.alertphone.features.onboarding.username

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hubertpawlowski.alertphone.features.onboarding.OnboardingViewModel
import com.hubertpawlowski.alertphone.features.onboarding.TopicStorage
import java.lang.IllegalArgumentException

class UserNameViewModelFactory (private val userNameStorage: UserNameStorage): ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(UserNameViewModel::class.java))
                return UserNameViewModel(userNameStorage) as T
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
