package com.id.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
    @SerialName("event_id") val eventId: Int? = null,

    @SerialName("provider") val provider: String? = null
)
