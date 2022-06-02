package com.example.compose_remember.ui.user

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose_remember.ui.user.component.UserLazyList

@Composable
fun UserListScreen(
    viewModel: UserListViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsState(initial = UserListUiState.Success(emptyList()))

    when (uiState) {
        is UserListUiState.Success -> {
            val list = (uiState as UserListUiState.Success).list
            UserLazyList(list = list) { viewModel.fetch() }
        }
        is UserListUiState.Failure -> {

        }
    }
}
