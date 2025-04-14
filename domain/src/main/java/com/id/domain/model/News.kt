package com.id.domain.model

data class News(
    val id: Int = 0,
    val title: String = "",
    val authors: List<Author> = emptyList(),
    val url: String = "",
    val imageUrl: String = "",
    val newsSite: String = "",
    val summary: String = "",
    val publishedAt: String = "",
    val updatedAt: String = "",
    val featured: Boolean = false,
    val launches: List<Launch> = emptyList(),
    val events: List<Event> = emptyList()
)

data class Author(
    val name: String = "", val socials: Socials? = null
)

data class Socials(
    val x: String? = null,
    val youtube: String? = null,
    val instagram: String? = null,
    val linkedin: String? = null,
    val mastodon: String? = null,
    val bluesky: String? = null
)

data class Launch(
    val launchId: String = "", val provider: String = ""
)

data class Event(
    val eventId: Int = 0, val provider: String = ""
)