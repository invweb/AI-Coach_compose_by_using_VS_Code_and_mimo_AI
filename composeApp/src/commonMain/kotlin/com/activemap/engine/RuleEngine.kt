package com.activemap.engine

import com.activemap.model.Place
import com.activemap.model.PlaceType
import com.activemap.model.Recommendation
import com.activemap.platform.WeatherInfo
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object RuleEngine {

    fun generateRecommendations(
        context: RuleContext,
        places: List<Place>,
        maxResults: Int = 3
    ): List<Recommendation> {
        val recommendations = mutableListOf<Recommendation>()

        for (place in places) {
            val matchedRule = allRules.firstOrNull { rule ->
                rule.evaluate(context, place) != null
            }

            val recommendation = matchedRule?.evaluate(context, place)
                ?: defaultRule.evaluate(context, place)

            recommendations.add(recommendation)
        }

        return recommendations
            .sortedByDescending { it.score }
            .take(maxResults)
    }

    fun buildContext(
        weather: WeatherInfo,
        recentActivityCount: Int,
        lastActivityDaysAgo: Int,
        placesCount: Map<com.activemap.model.PlaceType, Int>,
        totalActivitiesThisWeek: Int,
        challengeStreak: Int
    ): RuleContext {
        val now = Clock.System.now()
        val localTime = now.toLocalDateTime(TimeZone.currentSystemDefault()).time

        return RuleContext(
            currentTime = localTime,
            weather = weather,
            recentActivityCount = recentActivityCount,
            lastActivityDaysAgo = lastActivityDaysAgo,
            placesCount = placesCount,
            totalActivitiesThisWeek = totalActivitiesThisWeek,
            challengeStreak = challengeStreak
        )
    }
}
