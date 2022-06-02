package com.sample.extension

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("app:url")
    fun setImageUrl(view: ImageView, url: String?) {
        Glide.with(view)
            .load(url)
            .circleCrop()
            .into(view)
    }
}
