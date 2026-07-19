package com.activemap.platform

import com.activemap.model.GeoLocation

class DesktopLocationProvider : LocationProvider {
    override suspend fun getCurrentLocation(): GeoLocation {
        return GeoLocation(55.7558, 37.6173)
    }

    override suspend fun hasPermission(): Boolean = true

    override suspend fun requestPermission(): Boolean = true
}
