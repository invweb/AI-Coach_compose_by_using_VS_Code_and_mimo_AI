package com.activemap.platform

import com.activemap.model.GeoLocation

expect fun createLocationProvider(): LocationProvider
expect fun createWeatherProvider(): WeatherProvider
expect fun createFileExporter(): FileExporter

interface LocationProvider {
    suspend fun getCurrentLocation(): GeoLocation?
    suspend fun hasPermission(): Boolean
    suspend fun requestPermission(): Boolean
}

data class WeatherInfo(
    val temperature: Double,
    val condition: String,
    val humidity: Int = 50
)

interface WeatherProvider {
    suspend fun getCurrentWeather(): WeatherInfo
}

interface FileExporter {
    suspend fun exportMarkdown(content: String, filename: String): String
    suspend fun readMarkdown(filename: String): String?
}
