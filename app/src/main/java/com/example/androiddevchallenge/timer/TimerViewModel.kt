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

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.TimeUnit

class TimerViewModel : ViewModel() {
    private var _timer: CountDownTimer? = null
    private var _inputNumberText = "0"
    private var _periodSeconds = 0

    private val _timeText = MutableLiveData("00h 00m 00s")
    val timeText: LiveData<String> = _timeText

    private val _progress = MutableLiveData(0f)
    val progress: LiveData<Float> = _progress

    private val _progressText = MutableLiveData("")
    val progressText: LiveData<String> = _progressText

    private val _timerStarted = MutableLiveData(false)
    val timerStarted: LiveData<Boolean> = _timerStarted

    fun onNumberButtonClicked(numberKey: Int) {
        if (numberKey == -1) { // backspace key
            _inputNumberText = if (_inputNumberText.length >= 2) {
                _inputNumberText.substring(1)
            } else {
                "0"
            }
        } else {
            if (_inputNumberText.length < 6) {
                _inputNumberText = if (_inputNumberText == "0") {
                    "$numberKey"
                } else {
                    "${_inputNumberText}$numberKey"
                }
            }
        }

        val inputNumber = _inputNumberText.toInt()
        val h = inputNumber / 100 / 100
        val m = inputNumber / 100 % 100
        val s = inputNumber % 100
        _timeText.value = String.format("%02dh %02dm %02ds", h, m, s)
        _periodSeconds =
            (TimeUnit.HOURS.toSeconds(h.toLong()) + TimeUnit.MINUTES.toSeconds(m.toLong()) + s).toInt()
    }

    fun onStartButtonClick() {
        if (_timerStarted.value == true) {
            _timerStarted.value = false
            stopTimer()
        } else {
            _timerStarted.value = true
            if (_periodSeconds == 0) {
                _periodSeconds = 10
            }
            startTimer(_periodSeconds)
        }
    }

    private fun startTimer(seconds: Int) {
        _timer?.cancel()

        val millisInFuture = TimeUnit.SECONDS.toMillis(seconds.toLong())
        _timer = object : CountDownTimer(millisInFuture, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _progress.value =
                    1.0f - (millisInFuture - millisUntilFinished).toFloat() / millisInFuture.toFloat()

                val secsInFuture = millisUntilFinished / 1000
                val h = secsInFuture / 60 / 60
                val m = secsInFuture / 60 % 60
                val s = secsInFuture % 60
                _progressText.value = String.format("%02d:%02d:%02d", h, m, s)
            }

            override fun onFinish() {
                _progress.value = 1.0f
                _progressText.value = "Time's up"
            }
        }

        _timer?.start()
    }

    private fun stopTimer() {
        _timer?.cancel()
    }
}
