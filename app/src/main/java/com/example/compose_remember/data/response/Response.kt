package com.example.compose_remember.data.response

import com.google.gson.annotations.SerializedName

data class Response<T>(
    @SerializedName("total_count") val count: Int,
    @SerializedName("items") val items: List<T>
)
