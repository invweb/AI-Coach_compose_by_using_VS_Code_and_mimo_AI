package com.activemap.platform

class AndroidWeatherProvider : WeatherProvider {
    override suspend fun getCurrentWeather(): WeatherInfo {
        return WeatherInfo(
            temperature = 22.0,
            condition = "SUNNY",
            humidity = 45
        )
    }
}
