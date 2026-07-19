package com.activemap.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
enum class ChallengeType {
    WALK, RUN, STRETCH, PLANK, SQUATS, CARDIO
}

@Serializable
data class Challenge(
    val id: Long = 0,
    val title: String,
    val description: String,
    val type: ChallengeType,
    val completed: Boolean = false,
    val assignedDate: LocalDate
)
