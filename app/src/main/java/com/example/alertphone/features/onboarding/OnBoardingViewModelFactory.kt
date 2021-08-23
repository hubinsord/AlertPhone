package com.example.alertphone.features.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class OnBoardingViewModelFactory(private val topicStorage: TopicStorage): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(OnboardingActivity::class.java))
            return OnboardingViewModel(topicStorage) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}