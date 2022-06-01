package com.sample.extension

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.onScrollBottom(action: () -> Unit) {
    val layoutManager = (layoutManager as? LinearLayoutManager) ?: return

    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val itemCount = layoutManager.itemCount
            val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()

            if (itemCount >= lastVisible.dec()) {
                action.invoke()
            }
        }
    })
}