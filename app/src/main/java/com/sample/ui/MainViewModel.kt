package com.sample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.data.api.GithubAPI
import com.sample.extension.asFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val api: GithubAPI
) : ViewModel() {

    init {
        api.getGithubUsers()
            .asFlow()
            .onEach { result ->
                result.onSuccess {

                }
                result.onFailure {

                }
            }.launchIn(viewModelScope)
    }
}
