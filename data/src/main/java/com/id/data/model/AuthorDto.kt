package com.id.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorDto(
    @SerialName("name")
    val name: String? = null,

    @SerialName("socials")
    val socials: SocialsDto? = null
)
