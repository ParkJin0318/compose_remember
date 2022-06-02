package com.example.compose_remember

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose_remember.data.usecase.GetUserUseCase
import com.example.compose_remember.data.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

sealed class MainUiState {
    data class Success(val list: List<UserListModel>) : MainUiState()
    data class Failure(val message: String?) : MainUiState()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    companion object {
        private const val NAME = "park"
        private const val START_PAGE = 1
        private const val PER_PAGE = 20
    }

    private var currentPage: Int? = START_PAGE
    private val userListModels = mutableListOf<UserListModel>()

    private val _state: MutableSharedFlow<MainUiState> = MutableSharedFlow()
    val state: SharedFlow<MainUiState> get() = _state

    init {
        fetch()
    }

    fun fetch() {
        if (userListModels.lastOrNull() == UserListModel.Loading || currentPage == null) return

        getUserUseCase(NAME, currentPage, PER_PAGE).onEach { result ->
            when (result) {
                is Result.Loading -> {
                    UserListModel.Loading
                        .let(userListModels::add)

                    _state.emit(MainUiState.Success(userListModels.toList()))
                }
                is Result.Success -> {
                    UserListModel.Loading
                        .let(userListModels::remove)

                    result.value
                        .map { UserListModel.User(it.name, it.profileImageUrl) }
                        .let(userListModels::addAll)

                    currentPage = currentPage?.inc()
                    _state.emit(MainUiState.Success(userListModels.toList()))
                }
                is Result.Failure -> {
                    currentPage = null
                    _state.emit(MainUiState.Failure(result.throwable.message))
                }
            }
        }.launchIn(viewModelScope)
    }
}