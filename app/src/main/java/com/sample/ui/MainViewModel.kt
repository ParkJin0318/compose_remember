package com.sample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.data.api.GithubAPI
import com.sample.data.response.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import javax.inject.Inject

sealed class MainUiState {
    data class Success(val list: List<UserListModel>) : MainUiState()
    data class Failure(val message: String?) : MainUiState()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val api: GithubAPI
) : ViewModel() {

    companion object {
        private const val NAME = "park"
        private const val START_PAGE = 1
        private const val PER_PAGE = 20
    }

    private var currentPage: Int? = START_PAGE

    private val userListModels = mutableListOf<UserListModel>()

    private val _state: MutableStateFlow<MainUiState> =
        MutableStateFlow(MainUiState.Success(emptyList()))
    val state: StateFlow<MainUiState> get() = _state

    init {
        fetch()
    }

    fun fetch() {
        if (userListModels.lastOrNull() == UserListModel.Loading || currentPage == null) return

        getUsers(NAME, currentPage, PER_PAGE)
            .transform { result ->
                when (result) {
                    is Result.Loading -> {
                        UserListModel.Loading
                            .let(userListModels::add)

                        emit(MainUiState.Success(userListModels.toList()))
                    }
                    is Result.Success -> {
                        UserListModel.Loading
                            .let(userListModels::remove)

                        result.value
                            .map { UserListModel.User(it.name, it.profileImageUrl) }
                            .let(userListModels::addAll)

                        currentPage = currentPage?.inc()
                        emit(MainUiState.Success(userListModels.toList()))
                    }
                    is Result.Failure -> {
                        currentPage = null
                        emit(MainUiState.Failure(result.throwable.message))
                    }
                }
            }
            .flowOn(Dispatchers.IO)
            .onEach(_state::emit)
            .launchIn(viewModelScope)
    }

    private fun getUsers(keyword: String, page: Int?, per: Int?): Flow<Result<List<UserResponse>>> =
        callbackFlow {
            trySendBlocking(Result.Loading)

            runCatching { api.getGithubUsers(keyword, page, per) }
                .onSuccess {
                    trySendBlocking(Result.Success(it.items))
                }.onFailure {
                    trySendBlocking(Result.Failure(it))
                }

            awaitClose()
        }

    private sealed class Result<out T> {

        data class Success<T>(val value: T) : Result<T>()

        data class Failure(val throwable: Throwable) : Result<Nothing>()

        object Loading : Result<Nothing>()
    }
}
