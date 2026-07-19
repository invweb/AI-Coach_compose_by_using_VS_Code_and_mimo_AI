package com.activemap.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Recommendation(
    val id: Long = 0,
    val placeId: Long,
    val title: String,
    val description: String,
    val score: Double,
    val generatedAt: Instant = kotlinx.datetime.Clock.System.now()
)
