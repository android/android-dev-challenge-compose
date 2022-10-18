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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ads22.speedchallenge.data.Forecast
import com.google.ads22.speedchallenge.data.ForecastRepository
import com.google.ads22.speedchallenge.ui.location.LocationUiState.Error
import com.google.ads22.speedchallenge.ui.location.LocationUiState.Loading
import com.google.ads22.speedchallenge.ui.location.LocationUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val forecastRepository: ForecastRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val locationId = savedStateHandle.getStateFlow(LOCATION_ID_KEY, "Sunnyvale")

    val uiState = locationId.flatMapLatest { location ->
        forecastRepository.getForecast(location)
            .map<Forecast, LocationUiState> { Success(it) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun changeLocation(id: String) {
        viewModelScope.launch {
            savedStateHandle[LOCATION_ID_KEY] = id
        }
    }
    companion object {
        const val LOCATION_ID_KEY = "LOCATION_ID_KEY"
    }
}

sealed interface LocationUiState {
    object Loading : LocationUiState
    data class Error(val throwable: Throwable) : LocationUiState
    data class Success(
        val data: Forecast
    ) : LocationUiState
}
