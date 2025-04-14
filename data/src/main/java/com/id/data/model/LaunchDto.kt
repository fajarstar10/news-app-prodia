package com.id.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LaunchDto(
    @SerialName("launch_id")
    val launchId: String? = null,

    @SerialName("provider")
    val provider: String? = null
)
