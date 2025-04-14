package com.id.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("count")
    val count: Int? = 0,

    @SerialName("next")
    val next: String? = null,

    @SerialName("previous")
    val previous: String? = null,

    @SerialName("results")
    val results: List<T>? = emptyList()
)
