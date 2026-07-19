package com.activemap.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Report(
    val weekStart: Instant,
    val weekEnd: Instant,
    val totalActivities: Int,
    val totalDurationMinutes: Int,
    val placesVisited: List<String>,
    val challengesCompleted: Int,
    val challengesTotal: Int,
    val summaryMarkdown: String = ""
)
