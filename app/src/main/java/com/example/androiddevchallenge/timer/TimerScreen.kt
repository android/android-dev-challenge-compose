/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.timer

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@ExperimentalAnimationApi
@Composable
internal fun TimerScreen(timerViewModel: TimerViewModel = viewModel()) {
    val timeText: String by timerViewModel.timeText.observeAsState("")
    val progress: Float by timerViewModel.progress.observeAsState(initial = 0f)
    val progressText: String by timerViewModel.progressText.observeAsState("")
    val timerStarted: Boolean by timerViewModel.timerStarted.observeAsState(false)

    TimerContent(
        timeText = timeText,
        progress = progress,
        progressText = progressText,
        timerStarted = timerStarted,
        onStartButtonClick = { timerViewModel.onStartButtonClick() },
        onNumberChanged = { timerViewModel.onNumberButtonClicked(it) }
    )
}

@ExperimentalAnimationApi
@Composable
fun TimerContent(
    timeText: String,
    progress: Float,
    progressText: String,
    timerStarted: Boolean,
    onStartButtonClick: () -> Unit,
    onNumberChanged: (Int) -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    Crossfade(targetState = timerStarted) {
        if (timerStarted) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth()
                ) {
                    CircularProgressIndicator(
                        progress = animatedProgress,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(Modifier.requiredHeight(30.dp))

                Text(
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.weight(1f),
                    text = progressText
                )

                OutlinedButton(
                    onClick = onStartButtonClick
                ) {
                    Text(text = if (progress < 1.0f) "Stop" else "Reset")
                }
                Spacer(Modifier.requiredHeight(30.dp))
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .semantics(mergeDescendants = true) {},
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.weight(1f),
                        text = timeText
                    )
                    IconButton(
                        onClick = {
                            onNumberChanged(-1)
                        },
                        modifier = Modifier
                            .align(Alignment.Top)
                            .clearAndSetSemantics {}
                    ) {
                        Icon(Icons.Filled.Backspace, contentDescription = null)
                    }
                }
                Spacer(modifier = Modifier.size(20.dp))

                val numberModifier = Modifier
                    .weight(1f)
                    .width(44.dp)
                NumberKeyPad(modifier = numberModifier, onNumberChanged = onNumberChanged)
                Spacer(modifier = Modifier.size(20.dp))

                OutlinedButton(
                    onClick = onStartButtonClick,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        style = MaterialTheme.typography.body2,
                        text = if (timerStarted) "Stop" else "Start"
                    )
                }
            }
        }
    }
}

@Composable
private fun NumberKeyPad(modifier: Modifier, onNumberChanged: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
        ) {
            NumberTextButton(
                1,
                modifier = modifier,
                onNumberChanged = onNumberChanged
            )
            NumberTextButton(
                2,
                modifier = modifier,
                onNumberChanged = onNumberChanged
            )
            NumberTextButton(
                3,
                modifier = modifier,
                onNumberChanged = onNumberChanged
            )
        }
        Row(
            modifier = Modifier
                .padding(12.dp)
        ) {
            NumberTextButton(
                4,
                modifier = modifier,
                onNumberChanged = onNumberChanged
            )
            NumberTextButton(
                5,
                modifier = modifier,
                onNumberChanged = onNumberChanged
            )
            NumberTextButton(
                6,
                modifier = modifier,
                onNumberChanged = onNumberChanged
            )
        }
        Row(
            modifier = Modifier
                .padding(12.dp)
        ) {
            NumberTextButton(
                7,
                modifier = modifier,
                onNumberChanged = onNumberChanged
            )
            NumberTextButton(
                8,
                modifier = modifier,
                onNumberChanged = onNumberChanged
            )
            NumberTextButton(
                9,
                modifier = modifier,
                onNumberChanged = onNumberChanged
            )
        }
        Row(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Spacer(modifier = modifier)
            NumberTextButton(
                0, modifier = modifier, onNumberChanged = onNumberChanged
            )
            Spacer(modifier = modifier)
        }
    }
}

@Composable
private fun NumberTextButton(
    number: Int,
    modifier: Modifier,
    onNumberChanged: (Int) -> Unit
) {
    TextButton(
        onClick = {
            onNumberChanged(number)
        },
        modifier = modifier
    ) {
        Text("$number")
    }
}
