package com.adrianedilao.chesstimer.ui

import androidx.compose.ui.graphics.Color

data class MainState(
    /* White Timer States Below */
    var whiteTimerText: String,
    var whiteIsRunning: Boolean,
    var whiteTimeFinished: Boolean,
    var whiteTimerTextColor: Color,
    /* Black Timer States Below */
    var blackTimerText: String,
    var blackIsRunning: Boolean,
    var blackTimeFinished: Boolean,
    var blackTimerTextColor: Color,
    /* Global Timer States Below */
    var trueForWhiteFalseForBlack: Boolean,
    var resetTimer: Boolean,
    var gameModeTimeLength: Long
)
