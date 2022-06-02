package com.example.compose_remember.ui.user.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.compose_remember.extension.OnScrollBottom
import com.example.compose_remember.ui.user.UserListModel

@Composable
fun UserLazyList(
    list: List<UserListModel>,
    loadMore: () -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(state = listState) {
        items(list) { model ->
            when (model) {
                is UserListModel.User -> {
                    UserItem(
                        user = model,
                        modifier = Modifier.padding(all = 8.dp)
                    )
                }
                is UserListModel.Loading -> {
                    LoadingItem()
                }
            }
        }
    }

    listState.OnScrollBottom { loadMore() }
}