package com.example.compose_remember.extension

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*

@Composable
fun LazyListState.OnScrollBottom(
    loadMore: () -> Unit
) {
    val isLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem =
                layoutInfo.visibleItemsInfo.lastOrNull()
                    ?: return@derivedStateOf false

            lastVisibleItem.index == layoutInfo.totalItemsCount.dec()
        }
    }

    LaunchedEffect(isLoadMore) {
        snapshotFlow { isLoadMore.value }
            .collect {
                if (it) loadMore.invoke()
            }
    }
}
