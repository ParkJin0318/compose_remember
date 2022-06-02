package com.example.compose_remember.data.usecase

import com.example.compose_remember.data.api.GithubAPI
import com.example.compose_remember.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val api: GithubAPI
) {
    operator fun invoke(name: String, page: Int?, per: Int?) = callbackFlow {
        trySendBlocking(Result.Loading)

        runCatching { api.getGithubUsers(name, page, per) }
            .onSuccess {
                trySendBlocking(Result.Success(it.items))
            }.onFailure {
                trySendBlocking(Result.Failure(it))
            }
        awaitClose()
    }.flowOn(Dispatchers.IO)
}
