package com.id.data.mapper

import com.id.data.model.AuthorDto
import com.id.data.model.EventDto
import com.id.data.model.LaunchDto
import com.id.data.model.NewsDto
import com.id.data.model.SocialsDto
import com.id.domain.model.Author
import com.id.domain.model.Event
import com.id.domain.model.Launch
import com.id.domain.model.News
import com.id.domain.model.Socials

fun NewsDto.toDomain(): News = News(
    id = id ?: 0,
    title = title.orEmpty(),
    authors = authors?.map { it.toDomain() }.orEmpty(),
    url = url.orEmpty(),
    imageUrl = imageUrl.orEmpty(),
    newsSite = newsSite.orEmpty(),
    summary = summary.orEmpty(),
    publishedAt = publishedAt.orEmpty(),
    updatedAt = updatedAt.orEmpty(),
    featured = featured ?: false,
    launches = launches?.map { it.toDomain() }.orEmpty(),
    events = events?.map { it.toDomain() }.orEmpty()
)

fun AuthorDto.toDomain(): Author = Author(
    name = name.orEmpty(), socials = socials?.toDomain()
)

fun SocialsDto.toDomain(): Socials = Socials(
    x = x,
    youtube = youtube,
    instagram = instagram,
    linkedin = linkedin,
    mastodon = mastodon,
    bluesky = bluesky
)

fun LaunchDto.toDomain(): Launch = Launch(
    launchId = launchId.orEmpty(), provider = provider.orEmpty()
)

fun EventDto.toDomain(): Event = Event(
    eventId = eventId ?: 0, provider = provider.orEmpty()
)