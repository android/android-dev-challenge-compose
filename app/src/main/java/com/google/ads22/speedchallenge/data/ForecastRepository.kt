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

package com.google.ads22.speedchallenge.data

import com.google.ads22.speedchallenge.data.WeatherIcon.CLOUDY
import com.google.ads22.speedchallenge.data.WeatherIcon.OVERCAST
import com.google.ads22.speedchallenge.data.WeatherIcon.SUNNY
import com.google.ads22.speedchallenge.data.WeatherIcon.THUNDERSTORM
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface ForecastRepository {
    fun getForecast(locationId: String) : Flow<Forecast>
}

class DefaultForecastRepository @Inject constructor() : ForecastRepository {

    override fun getForecast(locationId: String): Flow<Forecast> = flow {
        when(locationId) {
            "Sunnyvale" -> emit(fakeForecast1)
            "Mountain View" -> emit(fakeForecast2)
            else -> throw IllegalArgumentException()
        }
    }
}

data class Location(
    val name: String,
//    var uid: String = ""
)

data class Forecast(
    val location: Location,
    val forecastToday: List<ForecastTime>,
    val forecastWeek: List<ForecastDay>
)

data class ForecastDay(
    val day: String,
    val maxTemp: Int,
    val minTemp: Int,
    val icon: WeatherIcon,
    val times: List<ForecastTime>
)

data class ForecastTime(
    val time: String,
    val temp: Int,
    val icon: WeatherIcon
)

enum class WeatherIcon {
    SUNNY, CLOUDY, THUNDERSTORM, OVERCAST
}

val fakeForecast1 = Forecast(
    location = Location("Sunnyvale"),
    forecastToday = listOf(
        ForecastTime(
            time = "8:00 AM",
            temp = 25,
            icon = OVERCAST
        ),
        ForecastTime(
            time = "10:00 AM",
            temp = 30,
            icon = SUNNY
        ),
        ForecastTime(
            time = "Noon",
            temp = 25,
            icon = SUNNY
        ),
        ForecastTime(
            time = "12:00 PM",
            temp = 25,
            icon = OVERCAST
        ),
        ForecastTime(
            time = "2:00 PM",
            temp = 25,
            icon = OVERCAST
        )
    ),
    forecastWeek = listOf(
        ForecastDay(
            day = "Tomorrow",
            maxTemp = 32,
            minTemp = 18,
            icon = SUNNY,
            generateForecastTimes(10)
        ),
        ForecastDay(
            day = "Tuesday",
            maxTemp = 22,
            minTemp = 14,
            icon = THUNDERSTORM,
            generateForecastTimes(15)
        ),

        ForecastDay(
            day = "Wednesday",
            maxTemp = 27,
            minTemp = 17,
            icon = CLOUDY,
            generateForecastTimes(20)
        ),
        ForecastDay(
            day = "Thursday",
            maxTemp = 32,
            minTemp = 18,
            icon = SUNNY,
            generateForecastTimes(25)
        ),
        ForecastDay(
            day = "Friday",
            maxTemp = 32,
            minTemp = 18,
            icon = SUNNY,
            generateForecastTimes(30)
        ),
        ForecastDay(
            day = "Saturday",
            maxTemp = 32,
            minTemp = 18,
            icon = SUNNY,
            generateForecastTimes(35)
        ),
    )
)
private fun generateForecastTimes(seed: Int): List<ForecastTime> {
    return listOf(
        ForecastTime(
            time = "8:00 AM",
            temp = seed + 0,
            icon = CLOUDY
        ),
        ForecastTime(
            time = "10:00 AM",
            temp = seed + 1,
            icon = SUNNY
        ),
        ForecastTime(
            time = "Noon",
            temp = seed + 2,
            icon = SUNNY
        ),
        ForecastTime(
            time = "12:00 PM",
            temp = seed + 3,
            icon = CLOUDY
        ),
        ForecastTime(
            time = "2:00 PM",
            temp = seed + 4,
            icon = CLOUDY
        )
    )
}

val fakeForecast2 = Forecast(
    location = Location("Mountain View"),
    forecastToday = listOf(
        ForecastTime(
            time = "8:00 AM",
            temp = 25,
            icon = CLOUDY
        ),
        ForecastTime(
            time = "10:00 AM",
            temp = 30,
            icon = SUNNY
        ),
        ForecastTime(
            time = "Noon",
            temp = 25,
            icon = SUNNY
        ),
        ForecastTime(
            time = "12:00 PM",
            temp = 25,
            icon = CLOUDY
        ),
        ForecastTime(
            time = "2:00 PM",
            temp = 25,
            icon = CLOUDY
        )
    ),
    forecastWeek = listOf(
        ForecastDay(
            day = "Tomorrow",
            maxTemp = 32,
            minTemp = 18,
            icon = SUNNY,
            generateForecastTimes(11)
        ),

        ForecastDay(
            day = "Wednesday",
            maxTemp = 32,
            minTemp = 18,
            icon = SUNNY,
            generateForecastTimes(16)
        ),

        ForecastDay(
            day = "Thursday",
            maxTemp = 32,
            minTemp = 18,
            icon = SUNNY,
            generateForecastTimes(21)
        )
    )
)
