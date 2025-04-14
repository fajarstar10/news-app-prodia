package com.id.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsDto(
    @SerialName("id")
    val id: Int? = null,

    @SerialName("title")
    val title: String? = null,

    @SerialName("authors")
    val authors: List<AuthorDto>? = emptyList(),

    @SerialName("url")
    val url: String? = null,

    @SerialName("image_url")
    val imageUrl: String? = null,

    @SerialName("news_site")
    val newsSite: String? = null,

    @SerialName("summary")
    val summary: String? = null,

    @SerialName("published_at")
    val publishedAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null,

    @SerialName("featured")
    val featured: Boolean? = null,

    @SerialName("launches")
    val launches: List<LaunchDto>? = emptyList(),

    @SerialName("events")
    val events: List<EventDto>? = emptyList()
)