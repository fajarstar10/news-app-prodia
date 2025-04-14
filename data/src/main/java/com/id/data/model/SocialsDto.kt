package com.id.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SocialsDto(
    @SerialName("x")
    val x: String? = null,
    @SerialName("youtube")
    val youtube: String? = null,
    @SerialName("instagram")
    val instagram: String? = null,
    @SerialName("linkedin")
    val linkedin: String? = null,
    @SerialName("mastodon")
    val mastodon: String? = null,
    @SerialName("bluesky")
    val bluesky: String? = null
)
