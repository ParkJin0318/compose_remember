package com.example.compose_remember.ui.user

sealed class UserListUiState {
    data class Success(val list: List<UserListModel>) : UserListUiState()
    data class Failure(val message: String?) : UserListUiState()
}
