package com.example.alertphone

import android.view.View
import androidx.databinding.BindingAdapter

object AppBindingAdapter {

    @BindingAdapter("visible")
    @JvmStatic
    fun setVisibility(view: View, value: Boolean) = view.run { visibility = if (value) View.VISIBLE else View.GONE }


}