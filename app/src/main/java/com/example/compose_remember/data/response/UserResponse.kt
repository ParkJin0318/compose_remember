package com.example.compose_remember.data.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("login") val name: String,
    @SerializedName("avatar_url") val profileImageUrl: String
)
