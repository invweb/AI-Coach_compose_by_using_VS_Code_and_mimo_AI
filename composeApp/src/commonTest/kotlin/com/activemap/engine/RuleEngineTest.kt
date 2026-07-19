package com.activemap.engine

import com.activemap.model.Place
import com.activemap.model.PlaceType
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.core.spec.style.StringSpec
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime

class RuleEngineTest : StringSpec({

    "MorningParkRun should recommend for park in morning" {
        val context = RuleContext(
            currentTime = LocalTime(7, 0),
            weather = com.activemap.platform.WeatherInfo(20.0, "SUNNY"),
            recentActivityCount = 2,
            lastActivityDaysAgo = 1,
            placesCount = mapOf(PlaceType.PARK to 1),
            totalActivitiesThisWeek = 3,
            challengeStreak = 0
        )

        val park = Place(
            id = 1,
            name = "Central Park",
            latitude = 55.7558,
            longitude = 37.6173,
            type = PlaceType.PARK
        )

        val result = MorningParkRun.evaluate(context, park)
        result shouldBe com.activemap.model.Recommendation(
            placeId = 1,
            title = "Morning jog in Central Park",
            description = "Perfect weather for a morning run. Fresh air in the park will boost your energy!",
            score = 9.0
        )
    }

    "MorningParkRun should not recommend for park at noon" {
        val context = RuleContext(
            currentTime = LocalTime(12, 0),
            weather = com.activemap.platform.WeatherInfo(20.0, "SUNNY"),
            recentActivityCount = 2,
            lastActivityDaysAgo = 1,
            placesCount = mapOf(PlaceType.PARK to 1),
            totalActivitiesThisWeek = 3,
            challengeStreak = 0
        )

        val park = Place(
            id = 1,
            name = "Central Park",
            latitude = 55.7558,
            longitude = 37.6173,
            type = PlaceType.PARK
        )

        val result = MorningParkRun.evaluate(context, park)
        result shouldBe null
    }

    "AfternoonGymWorkout should recommend for gym in afternoon" {
        val context = RuleContext(
            currentTime = LocalTime(14, 0),
            weather = com.activemap.platform.WeatherInfo(20.0, "SUNNY"),
            recentActivityCount = 1,
            lastActivityDaysAgo = 2,
            placesCount = mapOf(PlaceType.GYM to 1),
            totalActivitiesThisWeek = 1,
            challengeStreak = 0
        )

        val gym = Place(
            id = 1,
            name = "Fitness Center",
            latitude = 55.7558,
            longitude = 37.6173,
            type = PlaceType.GYM
        )

        val result = AfternoonGymWorkout.evaluate(context, gym)
        result shouldNotBe null
        result?.title shouldBe "Gym session at Fitness Center"
    }

    "RuleEngine should generate recommendations sorted by score" {
        val context = RuleContext(
            currentTime = LocalTime(7, 0),
            weather = com.activemap.platform.WeatherInfo(20.0, "SUNNY"),
            recentActivityCount = 2,
            lastActivityDaysAgo = 1,
            placesCount = mapOf(PlaceType.PARK to 1, PlaceType.GYM to 1),
            totalActivitiesThisWeek = 3,
            challengeStreak = 0
        )

        val places = listOf(
            Place(id = 1, name = "Park", latitude = 55.7558, longitude = 37.6173, type = PlaceType.PARK),
            Place(id = 2, name = "Gym", latitude = 55.7658, longitude = 37.6273, type = PlaceType.GYM)
        )

        val recommendations = RuleEngine.generateRecommendations(context, places, maxResults = 3)
        recommendations shouldHaveSize 2
        recommendations.first().score shouldBeGreaterThan recommendations.last().score
    }
})
