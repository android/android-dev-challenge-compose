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

@file:OptIn(ExperimentalMaterial3Api::class)

package com.google.ads22.speedchallenge.ui.location

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.ads22.speedchallenge.R
import com.google.ads22.speedchallenge.data.Forecast
import com.google.ads22.speedchallenge.data.ForecastDay
import com.google.ads22.speedchallenge.data.ForecastTime
import com.google.ads22.speedchallenge.data.WeatherIcon
import com.google.ads22.speedchallenge.data.WeatherIcon.CLOUDY
import com.google.ads22.speedchallenge.data.WeatherIcon.OVERCAST
import com.google.ads22.speedchallenge.data.WeatherIcon.SUNNY
import com.google.ads22.speedchallenge.data.WeatherIcon.THUNDERSTORM
import com.google.ads22.speedchallenge.data.fakeForecast1
import com.google.ads22.speedchallenge.ui.location.LocationUiState.Loading
import com.google.ads22.speedchallenge.ui.location.LocationUiState.Success
import com.google.ads22.speedchallenge.ui.theme.MyApplicationTheme

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun LocationScreen(modifier: Modifier = Modifier, viewModel: LocationViewModel = hiltViewModel()) {

    val forecast: State<LocationUiState> = viewModel.uiState.collectAsStateWithLifecycle(Loading)

    when (forecast.value) {
        is Success -> {
            LocationScreenForecast(
                (forecast.value as Success).data,
                onLocationChange = viewModel::changeLocation,
                modifier = modifier
            )
        }
        is Loading -> Text("Loading...")
        is LocationUiState.Error -> Text("Error!")
    }
}

@Composable
fun LocationScreenForecast(
    locationForecast: Forecast,
    onLocationChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val expandedDays = remember { mutableStateMapOf<String, Int>() }
    LocationScreenForecast(
        forecast = locationForecast,
        onLocationChange = onLocationChange,
        expandedDayIndex = expandedDays[locationForecast.location.name] ?: -1,
        onExpandedChanged = { expandedDays[locationForecast.location.name] = it },
        modifier = modifier
    )
}

val indexSaver = listSaver<MutableMap<String, Int>, Pair<String, Int>>(
    save = { it -> it.entries.map { it.key to it.value } },
    restore = { mutableStateMapOf(*it.toTypedArray()) }
)

@Composable
fun LocationScreenForecast(
    forecast: Forecast,
    onLocationChange: (name: String) -> Unit,
    expandedDayIndex: Int,
    onExpandedChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {

        TopAppBar(
            forecast.location.name,
            onLocationChange,
            Modifier.align(CenterHorizontally)
        )

        Today(forecast.forecastToday)

        val label = stringResource(
            id = R.string.weekly_forecast_cd
        )
        Column(modifier = Modifier
            .padding(horizontal = 16.dp)
            .semantics {
                contentDescription = label
            }) {
            val index = 0
            WeekForecastRow(
                forecast.forecastWeek[index],
                expanded = index == expandedDayIndex,
                onClick = { onExpandedChanged(if (expandedDayIndex == index) -1 else index) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    locationName: String,
    onLocationChange: (name: String) -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { onLocationChange("Mountain View") }
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowLeft,
                        stringResource(R.string.prev_location)
                    )
                }
                Text(
                    "//TODO",
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
                IconButton(
                    onClick = { onLocationChange("Sunnyvale") }
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowRight,
                        stringResource(R.string.next_location)
                    )
                }
            }
        }
    )
}

@Composable
fun Today(
    forecastTimes: List<ForecastTime>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        val now = forecastTimes.first()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(iconToDrawable(now.icon)),
                modifier = Modifier.width(122.dp),
                contentDescription = stringResource(iconToText(now.icon))
            )
            Spacer(Modifier.width(16.dp))
            Text(
                "${now.temp}°",
                style = MaterialTheme.typography.displayLarge.copy(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = Offset(3f, 3f),
                        blurRadius = 6f
                    )
                )
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .align(CenterHorizontally)
        ) {
            Text(
                stringResource(iconToText(now.icon)),
                style = MaterialTheme.typography.labelLarge
            )
            Text(stringResource(R.string.title_card_date))
        }
        DayForecastTimes(forecastTimes)

    }
}

@Composable
fun DayForecastTimes(forecastTimes: List<ForecastTime>) {
    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(top = 8.dp, bottom = 22.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        forecastTimes.forEachIndexed { index, forecastTime ->
            TodaysForecastCard(
                forecastTime = forecastTime,
                isSelected = false
            )
        }
    }
}

@Composable
private fun WeekForecastRow(
    forecastDay: ForecastDay,
    expanded: Boolean,
    onClick: () -> Unit
) {

    Surface(
        tonalElevation = if (expanded) 1.dp else 0.dp,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(top = 16.dp)
            .semantics {
                toggleableState = if (expanded) ToggleableState.On else ToggleableState.Off
            }
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .clickable {
                        onClick()
                    }
            ) {
                Text(
                    forecastDay.day,
                    modifier = Modifier
                        .weight(30f)
                        .padding(start = 16.dp),
                    style = MaterialTheme.typography.labelMedium
                )
                Row(
                    modifier = Modifier.weight(30f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painterResource(iconToDrawable(forecastDay.icon)),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.sizeIn(maxHeight = 38.dp, maxWidth = 38.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        stringResource(iconToText(forecastDay.icon)),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(
                    "${forecastDay.maxTemp}° | ${forecastDay.minTemp}°",
                    style = MaterialTheme.typography.labelMedium
                )
                Icon(
                    if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    if (expanded) {
                        stringResource(R.string.dropdown_less)
                    } else {
                        stringResource(R.string.dropdown_more)
                    }
                )
            }

            if (expanded) {
                DayForecastTimes(forecastDay.times)
            }
        }
    }
}

@Composable
fun TodaysForecastCard(
    forecastTime: ForecastTime,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundModifier = if (isSelected) Modifier.background(
        MaterialTheme.colorScheme.inversePrimary,
        MaterialTheme.shapes.small
    ) else Modifier
    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = modifier
            .then(backgroundModifier)
            .padding(4.dp)
            .size(60.dp, 96.dp)
    ) {
        Text(
            text = forecastTime.time,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.paddingFromBaseline(top = 18.dp, bottom = 12.dp)
        )
        Image(
            painterResource(iconToDrawable(forecastTime.icon)),
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .height(32.dp),
            contentDescription = stringResource(iconToText(forecastTime.icon))
        )
        Text(
            text = "${forecastTime.temp}°",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.paddingFromBaseline(top = 20.dp)
        )
    }
}

fun iconToText(icon: WeatherIcon): Int {
    return when (icon) {
        SUNNY -> R.string.icon_desc_sunny
        CLOUDY -> R.string.icon_desc_cloudy
        THUNDERSTORM -> R.string.icon_desc_thunderstorm
        OVERCAST -> R.string.icon_desc_overcast
    }
}

@DrawableRes
fun iconToDrawable(icon: WeatherIcon): Int {
    return when (icon) {
        SUNNY -> R.drawable.sun
        CLOUDY -> R.drawable.cloud
        THUNDERSTORM -> R.drawable.thunderstorm
        OVERCAST -> R.drawable.sun_and_cloud
    }
}
// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        LocationScreenForecast(locationForecast = fakeForecast1, onLocationChange = {})
    }
}

@Preview(showBackground = true)
@Composable
fun TopAppBarPreview() {
    MyApplicationTheme {
        Row(Modifier.fillMaxWidth()) {
            TopAppBar(locationName = "Sunnyvale", onLocationChange = {})
        }
    }
}

@Preview
@Composable
fun TodayPreview() {
    MyApplicationTheme {
        Today(fakeForecast1.forecastToday)
    }
}

@Preview(showBackground = true)
@Composable
fun WeekForecastPreview() {
    MyApplicationTheme {
        WeekForecastRow(
            ForecastDay(
                day = "Tomorrow",
                maxTemp = 32,
                minTemp = 18,
                icon = SUNNY,
                times = fakeForecast1.forecastToday
            ),
            expanded = true
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun TodaysForecastCardPreview() {
    MyApplicationTheme {
        TodaysForecastCard(
            forecastTime = ForecastTime(
                time = "8:00 AM",
                temp = 25,
                icon = CLOUDY
            ),
            isSelected = true
        )
    }
}
