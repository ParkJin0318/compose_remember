package com.example.compose_remember.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.compose_remember.MainUiState
import com.example.compose_remember.MainViewModel
import com.example.compose_remember.UserListModel
import com.google.accompanist.coil.rememberCoilPainter
import kotlinx.coroutines.flow.collect

@Composable
fun UserList(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.state.collectAsState(initial = MainUiState.Success(emptyList()))
    val listState = rememberLazyListState()

    when (uiState) {
        is MainUiState.Success -> {
            val list = (uiState as MainUiState.Success).list
            LazyColumn(
                modifier = modifier,
                state = listState
            ) {
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

            listState.OnScrollBottom {
                viewModel.fetch()
            }
        }
        is MainUiState.Failure -> {

        }
    }
}

@Composable
fun UserItem(
    user: UserListModel.User,
    modifier: Modifier = Modifier
) {
    val painter = rememberCoilPainter(request = user.profileImageUrl)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .clip(shape = CircleShape)
        )

        Text(
            text = user.name,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun LoadingItem() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun LazyListState.OnScrollBottom(
    loadMore: () -> Unit
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem =
                layoutInfo.visibleItemsInfo.lastOrNull()
                    ?: return@derivedStateOf false

            lastVisibleItem.index == layoutInfo.totalItemsCount.dec()
        }
    }
    
    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect {
                if (it) loadMore.invoke()
            }
    }
}