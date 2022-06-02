package com.example.compose_remember

sealed class UserListModel {

    data class User(val name: String, val profileImageUrl: String) : UserListModel()

    object Loading : UserListModel()
}
