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
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.ads22.speedchallenge.R
import com.google.ads22.speedchallenge.data.fakeForecast1
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI tests for [LocationScreen].
 */
@RunWith(AndroidJUnit4::class)
class LocationScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private var lastNavigated = ""

    @Before
    fun setup() {
        composeTestRule.setContent {
            LocationScreenForecast(
                forecast = fakeForecast1,
                onLocationChange = { lastNavigated = it },
                expandedDayIndex = -1,
                onExpandedChanged = {}
            )
        }
    }

    @Test
    fun title_exists() {
        composeTestRule.onNodeWithText(fakeForecast1.location.name).assertExists()
    }

    @Test
    fun changeLocation_right() {
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.next_location)
        ).performClick()
        assertEquals(lastNavigated, "Mountain View")
    }

    @Test
    fun changeLocation_left() {
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.prev_location)
        ).performClick()
        assertEquals(lastNavigated, "Sunnyvale")
    }
}
