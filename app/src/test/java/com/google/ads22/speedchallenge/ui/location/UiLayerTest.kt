/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.ads22.speedchallenge.ui.location

import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isOn
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.ads22.speedchallenge.R
import com.google.ads22.speedchallenge.data.fakeForecast1
import com.google.ads22.speedchallenge.data.fakeForecast2
import com.google.ads22.speedchallenge.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/**
 * UI tests for the UI Layer: [LocationScreen] and [LocationViewModel].
 */
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class, qualifiers = "xlarge")
class UiLayerTest {

    @get:Rule(order = 0)
    val hilt = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var weeklyForecastCd: String

    @Before
    fun loadResources() {
        weeklyForecastCd = composeTestRule.activity.resources.getString(R.string.weekly_forecast_cd)
    }

    @Test
    fun weeklyForecast_allDaysDisplayed() {
        // Find all the days in the week forecast
        val weekdays = composeTestRule.onAllNodes(
            hasClickAction() and hasAnyAncestor(hasContentDescription(weeklyForecastCd))
        )

        // Make sure all of them are there... not, say, just one.
        assertEquals(weekdays.fetchSemanticsNodes().size, fakeForecast1.forecastWeek.size)
    }

    @Test
    fun dayItemExpandedState_onNavigation_persistedAcrossLocations() {
        // Expand one of the days (the third)
        composeTestRule
            .onNodeWithText(fakeForecast1.forecastWeek[2].day, useUnmergedTree = true)
            .performClick()

        // Verify the item is expanded
        verifyTheresOneWeekDayExpanded()

        // Navigate to another screen
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.next_location)
        ).performClick()

        // Verify no item is expanded in this screen
        verifyNoWeekDayExpanded()

        // Navigate to previous screen
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.prev_location)
        ).performClick()

        // Verify the item is still expanded
        verifyTheresOneWeekDayExpanded()
    }

    @Test
    fun dayItemExpanded_onClick_collapses() {
        // Expand one of the days (the second)
        composeTestRule
            .onNodeWithText(fakeForecast1.forecastWeek[1].day, useUnmergedTree = true)
            .performClick()

        // Verify the item is expanded
        verifyTheresOneWeekDayExpanded()

        // Click on it again to collapse it
        composeTestRule
            .onNodeWithText(fakeForecast1.forecastWeek[1].day, useUnmergedTree = true)
            .performClick()

        // Verify no item is expanded in this screen
        verifyNoWeekDayExpanded()
    }

    @Test
    fun dayItemExpanded_onClickOnAnother_firstCollapsesSecondExpands() {
        // Expand one of the days (the second)
        composeTestRule
            .onNodeWithText(fakeForecast1.forecastWeek[1].day, useUnmergedTree = true)
            .performClick()

        verifyWeekdayExpanded(fakeForecast1.forecastWeek[1].day)

        // Click on another one to collapse it
        composeTestRule
            .onNodeWithText(fakeForecast1.forecastWeek[0].day, useUnmergedTree = true)
            .performClick()

        verifyWeekdayCollapsed(fakeForecast1.forecastWeek[1].day)
        verifyWeekdayExpanded(fakeForecast1.forecastWeek[0].day)

    }

    private fun verifyWeekdayExpanded(day: String) {
        composeTestRule.onNode(
            hasText(day) and
                hasAnyAncestor(hasContentDescription(weeklyForecastCd)) and
                hasAnyAncestor(isOn())

        ).assertExists()
    }

    private fun verifyWeekdayCollapsed(day: String) {
        composeTestRule.onNode(
            hasText(day) and
                hasAnyAncestor(hasContentDescription(weeklyForecastCd)) and
                hasAnyAncestor(isOn())

        ).assertDoesNotExist()
    }

    private fun verifyTheresOneWeekDayExpanded() {
        composeTestRule.onNode(
            hasText(fakeForecast1.forecastToday.first().time) and // TODO: ideally don't use first()
                hasAnyAncestor(hasContentDescription(weeklyForecastCd))
        ).assertExists()
    }

    private fun verifyNoWeekDayExpanded() {
        composeTestRule.onNode(
            hasText(fakeForecast1.forecastToday.first().time) and
                hasAnyAncestor(hasContentDescription(weeklyForecastCd))
        ).assertDoesNotExist()
    }

    @Test
    fun changeLocation_right() {
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.next_location)
        ).performClick()

        composeTestRule.onNodeWithText(fakeForecast2.location.name).assertExists()
    }

    @Test
    fun changeLocation_left() {
        changeLocation_right()
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.prev_location)
        ).performClick()

        composeTestRule.onNodeWithText(fakeForecast1.location.name).assertExists()
    }
}
