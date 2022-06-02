package com.sample.data.api

import com.sample.data.response.Response
import com.sample.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubAPI {

    @GET("search/users")
    suspend fun getGithubUsers(
        @Query("q", encoded = true) keyword: String,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?
    ): Response<UserResponse>
}
