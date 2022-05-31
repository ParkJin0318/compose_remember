package com.sample.extension

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> Call<T>.asFlow(): Flow<Result<T>> = callbackFlow {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                response.body()
                    ?.also { trySendBlocking(Result.success(it)) }
                    ?: trySendBlocking(Result.failure(Throwable()))
            } else {
                trySendBlocking(Result.failure(Throwable(response.errorBody().toString())))
            }
        }
        override fun onFailure(call: Call<T>, t: Throwable) {
            trySendBlocking(Result.failure(t))
        }
    })
    awaitClose()
}
