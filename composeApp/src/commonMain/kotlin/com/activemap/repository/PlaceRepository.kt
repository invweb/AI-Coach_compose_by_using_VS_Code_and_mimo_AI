package com.activemap.repository

import com.activemap.model.*
import kotlinx.coroutines.flow.Flow

interface PlaceRepository {
    fun getAllPlaces(): Flow<List<Place>>
    fun getPlacesByType(type: PlaceType): Flow<List<Place>>
    suspend fun getPlaceById(id: Long): Place?
    suspend fun insertPlace(place: Place): Long
    suspend fun deletePlace(id: Long)

    fun getRecentActivities(): Flow<List<Activity>>
    suspend fun insertActivity(activity: Activity): Long
    suspend fun completeActivity(id: Long, durationMinutes: Int)
    suspend fun getRecentActivityCount(days: Int): Int
    suspend fun getLastActivityDaysAgo(): Int
    suspend fun getWeeklyActivityCount(): Int

    fun getChallengesForDate(date: kotlinx.datetime.LocalDate): Flow<List<Challenge>>
    fun getRecentChallenges(): Flow<List<Challenge>>
    suspend fun insertChallenge(challenge: Challenge): Long
    suspend fun completeChallenge(id: Long)
    suspend fun getChallengeStreak(): Int

    suspend fun getPlacesCountByType(): Map<PlaceType, Int>

    suspend fun getWeatherCache(): Weather?
    suspend fun cacheWeather(weather: Weather)

    suspend fun getAllPlacesList(): List<Place>
}
