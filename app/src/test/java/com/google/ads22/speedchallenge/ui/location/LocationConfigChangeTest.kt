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

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.ads22.speedchallenge.R
import com.google.ads22.speedchallenge.data.fakeForecast1
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode

/**
 * State restoration tests for [LocationScreen].
 */
@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
class LocationConfigChangeTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var weeklyForecastCd: String

    @Before
    fun loadResources() {
        weeklyForecastCd = composeTestRule.activity.resources.getString(R.string.weekly_forecast_cd)
    }

    @Test
    fun locationConfigChange_stateRestored() {
        val stateRestorationTester = StateRestorationTester(composeTestRule)
        stateRestorationTester.setContent {
            LocationScreenForecast(
                fakeForecast1,
                { },
            )
        }
        verifyNoWeekDayExpanded()

        // Expand one of the items
        composeTestRule
            .onNodeWithText(fakeForecast1.forecastWeek[1].day, useUnmergedTree = true)
            .performClick()

        // Verify the item is expanded
        verifyTheresOneWeekDayExpanded()

        // Trigger state restoration
        stateRestorationTester.emulateSavedInstanceStateRestore()

        // Verify item still expanded
        verifyTheresOneWeekDayExpanded()
    }

    private fun verifyTheresOneWeekDayExpanded() {
        composeTestRule.onNode(
            hasText(fakeForecast1.forecastToday.first().time) and
                hasAnyAncestor(hasContentDescription(weeklyForecastCd))
        ).assertExists()
    }

    private fun verifyNoWeekDayExpanded() {
        composeTestRule.onNode(
            hasText(fakeForecast1.forecastToday.first().time) and
                hasAnyAncestor(hasContentDescription(weeklyForecastCd))
        ).assertDoesNotExist()
    }
}
