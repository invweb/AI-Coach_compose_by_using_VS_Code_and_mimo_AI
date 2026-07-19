package com.activemap.platform

actual fun createLocationProvider(): LocationProvider {
    return DesktopLocationProvider()
}

actual fun createWeatherProvider(): WeatherProvider {
    return DesktopWeatherProvider()
}

actual fun createFileExporter(): FileExporter {
    return DesktopFileExporter()
}
