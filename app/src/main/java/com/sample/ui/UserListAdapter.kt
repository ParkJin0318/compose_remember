package com.sample.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sample.R
import com.sample.databinding.ViewItemLoadingBinding
import com.sample.databinding.ViewItemUserBinding

class UserListAdapter : ListAdapter<UserListModel, UserListAdapter.ViewHolder>(UserListDiffCallback) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is UserListModel.User -> R.layout.view_item_user
        is UserListModel.Loading -> R.layout.view_item_loading
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = when (viewType) {
            R.layout.view_item_user -> {
                ViewItemUserBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }
            else -> {
                ViewItemLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }
        }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        when (val binding = holder.binding) {
            is ViewItemUserBinding -> binding.model = item as UserListModel.User
            is ViewItemLoadingBinding -> binding.model = item as UserListModel.Loading
        }
    }

    class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
}