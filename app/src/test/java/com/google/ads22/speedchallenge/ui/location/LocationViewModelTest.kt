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


import androidx.lifecycle.SavedStateHandle
import com.google.ads22.speedchallenge.data.DefaultForecastRepository
import com.google.ads22.speedchallenge.data.Forecast
import com.google.ads22.speedchallenge.data.ForecastRepository
import com.google.ads22.speedchallenge.ui.location.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class LocationViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainDispatcherRule()

    @Test
    fun uiState_initiallyLoading() = runTest {
        val viewModel = LocationViewModel(FakeNoopForecastRepository(), SavedStateHandle())
        assertEquals(viewModel.uiState.first(), LocationUiState.Loading)
    }

    @Test
    fun uiState_error() = runTest {
        // Given a LocationViewModel
        val viewModel = LocationViewModel(
            DefaultForecastRepository(),
            SavedStateHandle()
        )

        // Create an empty collector for the StateFlow
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        // Change the location to an invalid one
        viewModel.changeLocation("Invalid")

        // Verify the new value is an error
        assertTrue(viewModel.uiState.value is LocationUiState.Error)

        // Cancel the collecting job at the end of the test
        collectJob.cancel()
    }
}

class FakeNoopForecastRepository @Inject constructor() : ForecastRepository {

    override fun getForecast(locationId: String): Flow<Forecast> = flow { }
}
