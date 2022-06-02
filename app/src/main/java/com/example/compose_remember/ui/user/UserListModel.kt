package com.example.compose_remember.ui.user

sealed class UserListModel {

    data class User(
        val name: String,
        val profileImageUrl: String,
        val onClick: (String) -> Unit
    ) : UserListModel()

    object Loading : UserListModel()
}
