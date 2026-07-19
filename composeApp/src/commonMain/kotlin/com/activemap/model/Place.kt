package com.activemap.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
enum class PlaceType {
    PARK, STADIUM, GYM, STREET
}

@Serializable
data class Place(
    val id: Long = 0,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val type: PlaceType,
    val description: String = "",
    val createdAt: Instant = kotlinx.datetime.Clock.System.now()
)
