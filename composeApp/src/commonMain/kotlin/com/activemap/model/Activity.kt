package com.activemap.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Activity(
    val id: Long = 0,
    val placeId: Long,
    val description: String,
    val startedAt: Instant,
    val completedAt: Instant? = null,
    val durationMinutes: Int = 0
)
