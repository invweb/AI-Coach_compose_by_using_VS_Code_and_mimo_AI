package com.activemap.engine

import com.activemap.model.Place
import com.activemap.model.PlaceType
import com.activemap.model.Recommendation
import com.activemap.platform.WeatherInfo
import kotlinx.datetime.LocalTime

sealed class Rule(
    val priority: Int,
    val description: String
) {
    abstract fun evaluate(
        context: RuleContext,
        place: Place
    ): Recommendation?
}

data class RuleContext(
    val currentTime: LocalTime,
    val weather: WeatherInfo,
    val recentActivityCount: Int,
    val lastActivityDaysAgo: Int,
    val placesCount: Map<PlaceType, Int>,
    val totalActivitiesThisWeek: Int,
    val challengeStreak: Int
)

object MorningParkRun : Rule(10, "Morning park run") {
    override fun evaluate(context: RuleContext, place: Place): Recommendation? {
        if (place.type != PlaceType.PARK) return null
        if (context.currentTime.hour !in 6..9) return null
        if (context.weather.condition == "RAIN" || context.weather.condition == "SNOW") return null
        return Recommendation(
            placeId = place.id,
            title = "Morning jog in ${place.name}",
            description = "Perfect weather for a morning run. Fresh air in the park will boost your energy!",
            score = 9.0
        )
    }
}

object AfternoonGymWorkout : Rule(9, "Afternoon gym session") {
    override fun evaluate(context: RuleContext, place: Place): Recommendation? {
        if (place.type != PlaceType.GYM) return null
        if (context.currentTime.hour !in 12..17) return null
        return Recommendation(
            placeId = place.id,
            title = "Gym session at ${place.name}",
            description = "Afternoon is great for strength training. Hit the gym!",
            score = 8.5
        )
    }
}

object EveningStadiumCardio : Rule(8, "Evening stadium cardio") {
    override fun evaluate(context: RuleContext, place: Place): Recommendation? {
        if (place.type != PlaceType.STADIUM) return null
        if (context.currentTime.hour !in 17..20) return null
        return Recommendation(
            placeId = place.id,
            title = "Cardio at ${place.name}",
            description = "Evening is ideal for cardio. The stadium track is waiting for you!",
            score = 8.0
        )
    }
}

object RainyDayGym : Rule(7, "Rainy day go to gym") {
    override fun evaluate(context: RuleContext, place: Place): Recommendation? {
        if (place.type != PlaceType.GYM) return null
        if (context.weather.condition != "RAIN" && context.weather.condition != "SNOW") return null
        return Recommendation(
            placeId = place.id,
            title = "Indoor workout at ${place.name}",
            description = "It's ${context.weather.condition.lowercase()} outside. Perfect time for indoor training!",
            score = 7.5
        )
    }
}

object HotDayPool : Rule(6, "Hot day outdoor exercise") {
    override fun evaluate(context: RuleContext, place: Place): Recommendation? {
        if (context.weather.temperature < 30.0) return null
        if (place.type == PlaceType.GYM) return null
        return Recommendation(
            placeId = place.id,
            title = "Outdoor activity at ${place.name}",
            description = "It's ${context.weather.temperature}°C! Stay hydrated and enjoy outdoor exercise.",
            score = 6.5
        )
    }
}

object ColdDayStreet : Rule(5, "Cold day avoid street") {
    override fun evaluate(context: RuleContext, place: Place): Recommendation? {
        if (place.type != PlaceType.STREET) return null
        if (context.weather.temperature > 5.0) return null
        return Recommendation(
            placeId = place.id,
            title = "Bundle up for ${place.name}",
            description = "It's cold (${context.weather.temperature}°C). Dress warm for your street workout!",
            score = 5.0
        )
    }
}

object RestDayRecommendation : Rule(4, "Rest day after heavy activity") {
    override fun evaluate(context: RuleContext, place: Place): Recommendation? {
        if (context.recentActivityCount < 5) return null
        return Recommendation(
            placeId = place.id,
            title = "Active recovery at ${place.name}",
            description = "You've been active lately. Consider a light walk or stretching today.",
            score = 7.0
        )
    }
}

object ComebackRecommendation : Rule(3, "Comeback after long break") {
    override fun evaluate(context: RuleContext, place: Place): Recommendation? {
        if (context.lastActivityDaysAgo <= 3) return null
        return Recommendation(
            placeId = place.id,
            title = "Welcome back! Try ${place.name}",
            description = "It's been ${context.lastActivityDaysAgo} days since your last activity. Start slow!",
            score = 9.5
        )
    }
}

object VarietyRecommendation : Rule(2, "Try a new place type") {
    override fun evaluate(context: RuleContext, place: Place): Recommendation? {
        val leastVisitedType = context.placesCount.entries
            .filter { it.value > 0 }
            .minByOrNull { it.value }
            ?.key ?: return null
        if (place.type != leastVisitedType) return null
        return Recommendation(
            placeId = place.id,
            title = "Explore ${place.name}",
            description = "You haven't visited many ${place.type.name.lowercase()} places. Give it a try!",
            score = 6.0
        )
    }
}

object WeekendChallenge : Rule(1, "Weekend special challenge") {
    override fun evaluate(context: RuleContext, place: Place): Recommendation? {
        if (context.currentTime.hour !in 8..11) return null
        return Recommendation(
            placeId = place.id,
            title = "Weekend challenge at ${place.name}",
            description = "Start your weekend with a special workout challenge!",
            score = 7.0
        )
    }
}

object MorningStretching : Rule(10, "Morning stretching") {
    override fun evaluate(context: RuleContext, place: Place): Recommendation? {
        if (context.currentTime.hour !in 6..8) return null
        if (place.type != PlaceType.PARK && place.type != PlaceType.GYM) return null
        return Recommendation(
            placeId = place.id,
            title = "Morning stretching at ${place.name}",
            description = "Start your day with gentle stretching. Your body will thank you!",
            score = 8.0
        )
    }
}

object StreakBoost : Rule(11, "Streak boost reward") {
    override fun evaluate(context: RuleContext, place: Place): Recommendation? {
        if (context.challengeStreak < 3) return null
        return Recommendation(
            placeId = place.id,
            title = "Streak reward at ${place.name}",
            description = "${context.challengeStreak}-day streak! Keep it going with a special session.",
            score = 10.0
        )
    }
}

object DefaultRecommendation : Rule(0, "Default suggestion") {
    override fun evaluate(context: RuleContext, place: Place): Recommendation {
        return Recommendation(
            placeId = place.id,
            title = "Visit ${place.name}",
            description = "A great place for your next activity. Check it out!",
            score = 3.0
        )
    }
}

val allRules: List<Rule> = listOf(
    StreakBoost,
    MorningParkRun,
    AfternoonGymWorkout,
    EveningStadiumCardio,
    RainyDayGym,
    HotDayPool,
    ColdDayStreet,
    RestDayRecommendation,
    ComebackRecommendation,
    VarietyRecommendation,
    WeekendChallenge,
    MorningStretching
)

val defaultRule = DefaultRecommendation
