package com.sample.extension

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.onScrollBottom(action: () -> Unit) {
    val layoutManager = (this.layoutManager as? LinearLayoutManager) ?: return

    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()

            if (lastVisiblePosition >= layoutManager.itemCount.dec()) {
                action.invoke()
            }
        }
    })
}