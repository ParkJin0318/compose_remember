package com.sample.ui

import androidx.recyclerview.widget.DiffUtil

sealed class UserListModel {

    data class User(
        val name: String,
        val profileImageUrl: String,
        val onClick: (String) -> Unit
    ) : UserListModel()

    object Loading : UserListModel()
}

object UserListDiffCallback : DiffUtil.ItemCallback<UserListModel>() {

    override fun areItemsTheSame(oldItem: UserListModel, newItem: UserListModel): Boolean {
        val isSameUser = oldItem is UserListModel.User &&
                newItem is UserListModel.User &&
                oldItem.name == newItem.name &&
                oldItem.profileImageUrl == newItem.profileImageUrl

        val isSameLoading = oldItem is UserListModel.Loading &&
                newItem is UserListModel.Loading &&
                oldItem == newItem

        return isSameUser || isSameLoading
    }

    override fun areContentsTheSame(oldItem: UserListModel, newItem: UserListModel): Boolean {
        return oldItem == newItem
    }
}