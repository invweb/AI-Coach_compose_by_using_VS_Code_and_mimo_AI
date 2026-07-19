package com.activemap.platform

class DesktopWeatherProvider : WeatherProvider {
    override suspend fun getCurrentWeather(): WeatherInfo {
        return WeatherInfo(
            temperature = 18.0,
            condition = "CLOUDY",
            humidity = 60
        )
    }
}
