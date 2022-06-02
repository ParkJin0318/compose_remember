package com.example.compose_remember.ui.user

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose_remember.ui.user.component.UserLazyList

@Composable
fun UserListScreen(
    viewModel: UserListViewModel = hiltViewModel()
) {
    val userListModels by viewModel.userListModels
        .collectAsState(initial = emptyList())

    val errorMessage by viewModel.errorMessage
        .collectAsState(initial = null)

    UserLazyList(list = userListModels) { viewModel.fetch() }
    showToast(LocalContext.current, errorMessage)
}

private fun showToast(context: Context, message: String?) {
    message?.let {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
