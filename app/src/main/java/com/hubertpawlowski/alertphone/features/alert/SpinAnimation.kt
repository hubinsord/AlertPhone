package com.hubertpawlowski.alertphone.features.alert

import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation

class SpinAnimation(offset: Float, duration: Long, repeatCount: Int) : RotateAnimation(offset,
    -offset,
    Animation.RELATIVE_TO_SELF,
    0.5f,
    Animation.RELATIVE_TO_SELF,
    0.5f) {
    init {
        interpolator = LinearInterpolator()
        repeatMode = Animation.REVERSE
        this.duration = duration
        this.repeatCount = repeatCount
    }
}