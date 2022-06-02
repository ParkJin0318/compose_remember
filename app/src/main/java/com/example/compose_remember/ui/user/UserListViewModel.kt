package com.example.compose_remember.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose_remember.data.Result
import com.example.compose_remember.data.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    companion object {
        private const val NAME = "park"
        private const val START_PAGE = 1
        private const val PER_PAGE = 20
    }

    private var currentPage: Int? = START_PAGE

    private val _userListModels: MutableStateFlow<List<UserListModel>> = MutableStateFlow(emptyList())
    val userListModels: StateFlow<List<UserListModel>> get() = _userListModels

    private val _errorMessage: MutableSharedFlow<String?> = MutableSharedFlow()
    val errorMessage: SharedFlow<String?> get() = _errorMessage

    init {
        fetch()
    }

    fun fetch() {
        val userListModels: MutableList<UserListModel> = _userListModels.value.toMutableList()
        if (userListModels.lastOrNull() == UserListModel.Loading || currentPage == null) return

        getUserUseCase(NAME, currentPage, PER_PAGE).onEach { result ->
            when (result) {
                is Result.Loading -> {
                    UserListModel.Loading
                        .let(userListModels::add)

                    _userListModels.emit(userListModels.toList())
                }
                is Result.Success -> {
                    UserListModel.Loading
                        .let(userListModels::remove)

                    result.value
                        .map { UserListModel.User(it.name, it.profileImageUrl, ::onClickUser) }
                        .let(userListModels::addAll)

                    currentPage = currentPage?.inc()
                    _userListModels.emit(userListModels.toList())
                }
                is Result.Failure -> {
                    currentPage = null
                    _errorMessage.emit(result.throwable.message)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun onClickUser(name: String) {
        val userListModels: MutableList<UserListModel> = _userListModels.value.toMutableList()
        val index = userListModels.filterIsInstance<UserListModel.User>()
            .indexOfFirst { it.name == name }

        userListModels.removeAt(index)

        viewModelScope.launch {
            _userListModels.emit(userListModels.toList())
        }
    }
}
