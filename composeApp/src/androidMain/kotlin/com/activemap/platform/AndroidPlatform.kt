package com.activemap.platform

import android.content.Context

actual fun createLocationProvider(): LocationProvider {
    throw UnsupportedOperationException("Use AndroidLocationProvider(context) directly")
}

actual fun createWeatherProvider(): WeatherProvider {
    return AndroidWeatherProvider()
}

actual fun createFileExporter(): FileExporter {
    throw UnsupportedOperationException("Use AndroidFileExporter(context) directly")
}

fun createAndroidLocationProvider(context: Context): LocationProvider {
    return AndroidLocationProvider(context)
}

fun createAndroidFileExporter(context: Context): FileExporter {
    return AndroidFileExporter(context)
}
