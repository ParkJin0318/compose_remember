package com.sample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.data.api.GithubAPI
import com.sample.extension.asFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

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

    private val _userListModels: MutableLiveData<List<UserListModel>> = MutableLiveData()
    val userListModels: LiveData<List<UserListModel>> get() = _userListModels

    private val _onFailure: MutableLiveData<String> = MutableLiveData()
    val onFailure: LiveData<String> get() = _onFailure

    init {
        load()
    }

    fun load() {
        if (userListModels.value?.lastOrNull() == UserListModel.Loading ||
            currentPage == null) return

        setLoading(true)

        api.getGithubUsers(NAME, currentPage, PER_PAGE)
            .asFlow()
            .distinctUntilChanged()
            .onEach { result ->
                setLoading(false)

                result.onSuccess { response ->
                    val list = _userListModels.value
                        ?.toMutableList()
                        ?.apply {
                            response.items
                                .map { UserListModel.User(it.name, it.profileImageUrl) }
                                .let(this::addAll)
                        }
                    _userListModels.value = list
                    currentPage = currentPage?.inc()
                }
                result.onFailure {
                    _onFailure.value = it.message
                    currentPage = null
                }
            }.launchIn(viewModelScope)
    }

    private fun setLoading(show: Boolean) {
        val model = UserListModel.Loading

        val list = _userListModels.value
            ?.toMutableList()
            ?.apply {
                if (show) add(model)
                else remove(model)
            }

        _userListModels.value = list
    }
}
