package com.sample.extension

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.onScrollBottom(action: () -> Unit) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (canScrollVertically(1).not()) {
                action.invoke()
            }
        }
    })
}
