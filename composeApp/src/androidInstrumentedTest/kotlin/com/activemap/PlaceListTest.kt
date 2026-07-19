package com.activemap

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.activemap.model.Place
import com.activemap.model.PlaceType
import com.activemap.ui.components.PlaceListItem
import com.activemap.ui.animation.AnimationConfig
import org.junit.Rule
import org.junit.Test

class PlaceListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun placeListItem_displaysCorrectly() {
        val place = Place(
            id = 1,
            name = "Test Park",
            latitude = 55.7558,
            longitude = 37.6173,
            type = PlaceType.PARK,
            description = "A test park"
        )

        composeTestRule.setContent {
            PlaceListItem(
                place = place,
                index = 0,
                animationConfig = AnimationConfig(enabled = false),
                onClick = {}
            )
        }

        composeTestRule.onNodeWithText("Test Park").assertIsDisplayed()
        composeTestRule.onNodeWithText("park • 55.7558, 37.6173").assertIsDisplayed()
        composeTestRule.onNodeWithText("A test park").assertIsDisplayed()
    }

    @Test
    fun placeListItem_handlesClick() {
        var clicked = false
        val place = Place(
            id = 1,
            name = "Clickable Place",
            latitude = 55.7558,
            longitude = 37.6173,
            type = PlaceType.GYM
        )

        composeTestRule.setContent {
            PlaceListItem(
                place = place,
                index = 0,
                animationConfig = AnimationConfig(enabled = false),
                onClick = { clicked = true }
            )
        }

        composeTestRule.onNodeWithText("Clickable Place").performClick()
        assert(clicked) { "Click handler was not called" }
    }

    @Test
    fun placeListItem_showsCorrectTypeIcon() {
        val place = Place(
            id = 1,
            name = "Gym",
            latitude = 55.7558,
            longitude = 37.6173,
            type = PlaceType.GYM
        )

        composeTestRule.setContent {
            PlaceListItem(
                place = place,
                index = 0,
                animationConfig = AnimationConfig(enabled = false),
                onClick = {}
            )
        }

        composeTestRule.onNodeWithText("Gym").assertIsDisplayed()
        composeTestRule.onNodeWithText("gym • 55.7558, 37.6173").assertIsDisplayed()
    }
}
