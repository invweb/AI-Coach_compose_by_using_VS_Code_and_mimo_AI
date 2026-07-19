package com.activemap.export

import com.activemap.model.Activity
import com.activemap.model.Challenge
import com.activemap.model.ChallengeType
import com.activemap.model.Place
import com.activemap.model.PlaceType
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.shouldNotBe
import io.kotest.core.spec.style.StringSpec
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus

class MarkdownExporterTest : StringSpec({

    "generateWeeklyReport should create valid markdown" {
        val now = Clock.System.now()
        val weekAgo = now.minus(period = DateTimePeriod(days = 7), timeZone = TimeZone.currentSystemDefault())

        val activities = listOf(
            Activity(
                id = 1,
                placeId = 1,
                description = "Morning run",
                startedAt = now.minus(period = DateTimePeriod(days = 1), timeZone = TimeZone.currentSystemDefault()),
                completedAt = now.minus(period = DateTimePeriod(days = 1), timeZone = TimeZone.currentSystemDefault()),
                durationMinutes = 30
            )
        )

        val challenges = listOf(
            Challenge(
                id = 1,
                title = "10-Minute Walk",
                description = "Take a brisk 10-minute walk",
                type = ChallengeType.WALK,
                completed = true,
                assignedDate = LocalDate(2024, 1, 15)
            )
        )

        val places = listOf(
            Place(
                id = 1,
                name = "Central Park",
                latitude = 55.7558,
                longitude = 37.6173,
                type = PlaceType.PARK,
                description = "Beautiful park in the city center"
            )
        )

        val report = MarkdownExporter.generateWeeklyReport(
            activities = activities,
            challenges = challenges,
            places = places,
            weekStart = weekAgo,
            weekEnd = now
        )

        report shouldNotBe ""
        report shouldContain "ActiveMap Weekly Report"
        report shouldContain "Morning run"
        report shouldContain "10-Minute Walk"
        report shouldContain "Central Park"
        report shouldContain "30min"
    }

    "generateWeeklyReport should handle empty data" {
        val now = Clock.System.now()
        val weekAgo = now.minus(period = DateTimePeriod(days = 7), timeZone = TimeZone.currentSystemDefault())

        val report = MarkdownExporter.generateWeeklyReport(
            activities = emptyList(),
            challenges = emptyList(),
            places = emptyList(),
            weekStart = weekAgo,
            weekEnd = now
        )

        report shouldNotBe ""
        report shouldContain "ActiveMap Weekly Report"
        report shouldContain "Total Activities:** 0"
        report shouldContain "Start your fitness journey today!"
    }
})
