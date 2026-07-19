package com.activemap.model

import kotlinx.serialization.Serializable

@Serializable
enum class WeatherCondition {
    SUNNY, CLOUDY, RAINY, SNOWY, WINDY, HOT, COLD
}

@Serializable
data class Weather(
    val temperature: Double,
    val condition: WeatherCondition
)
