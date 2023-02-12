package com.adrianedilao.chesstimer.ui

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    var whiteTimeInMilliSeconds = 1500000L //TEST
    var blackTimeInMilliSeconds = 1500000L //TEST

    var whiteMinute = (whiteTimeInMilliSeconds / 1000) / 60
    var whiteSeconds = (whiteTimeInMilliSeconds / 1000) % 60

    var blackMinute = (blackTimeInMilliSeconds / 1000) / 60
    var blackSeconds = (blackTimeInMilliSeconds / 1000) % 60

    private val _uiState = MutableStateFlow(
        MainState(
            whiteTimerText = "$whiteMinute:$whiteSeconds",
            whiteIsRunning = false,
            whiteTimeFinished = false,
            whiteTimerTextColor = Color.Black,
            blackTimerText = "$blackMinute:$blackSeconds",
            blackIsRunning = false,
            blackTimeFinished = false,
            blackTimerTextColor = Color.White,
            trueForWhiteFalseForBlack = true,
            resetTimer = false,
            gameModeTimeLength = 300000L
        )
    )

    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

    private lateinit var whiteCountdownTimer: CountDownTimer
    private lateinit var blackCountdownTimer: CountDownTimer

    /* Global Timer Functions Below */

    fun playOrPause(){
        if (_uiState.value.whiteIsRunning || _uiState.value.blackIsRunning){
            if (_uiState.value.whiteIsRunning){
                pauseWhiteTimer()
            }
            if (_uiState.value.blackIsRunning){
                pauseBlackTimer()
            }
        } else {
            if (_uiState.value.trueForWhiteFalseForBlack){
                startWhiteTimer()
                _uiState.update { currentState ->
                    currentState.copy(resetTimer = false)
                }
            } else {
                startBlackTimer()
                _uiState.update { currentState ->
                    currentState.copy(resetTimer = false)
                }
            }
        }
    }

    fun restartMatch(){
        Log.d("DEBUGGING","restartMatch()")
        whiteTimeInMilliSeconds = _uiState.value.gameModeTimeLength //TEST
        blackTimeInMilliSeconds = _uiState.value.gameModeTimeLength // TEST

        whiteMinute = (whiteTimeInMilliSeconds / 1000) / 60
        whiteSeconds = (whiteTimeInMilliSeconds / 1000) % 60

        blackMinute = (blackTimeInMilliSeconds / 1000) / 60
        blackSeconds = (blackTimeInMilliSeconds / 1000) % 60

        if (_uiState.value.whiteIsRunning){
            pauseWhiteTimer()
        }
        if (_uiState.value.blackIsRunning){
            pauseBlackTimer()
        }

        updateWhiteTimeTextUi()
        updateBlackTimeTextUi()

        _uiState.update { currentState ->
            currentState.copy(
                whiteTimerText = "$whiteMinute:$whiteSeconds",
                whiteIsRunning = false,
                whiteTimeFinished = false,
                whiteTimerTextColor = Color.Black,
                blackTimerText = "$blackMinute:$blackSeconds",
                blackIsRunning = false,
                blackTimeFinished = false,
                blackTimerTextColor = Color.White,
                trueForWhiteFalseForBlack = true,
                resetTimer = false
            )
        }
    }

    fun whiteSwitchTime(){
        if(_uiState.value.whiteIsRunning){
            pauseWhiteTimer()
            startBlackTimer()
            _uiState.update { currentState ->
                currentState.copy(trueForWhiteFalseForBlack = false)
            }
        } else {
            Log.d("NULL", "Null")
        }
    }

    fun blackSwitchTime(){
        if (_uiState.value.blackIsRunning){
            pauseBlackTimer()
            startWhiteTimer()
            _uiState.update { currentState ->
                currentState.copy(trueForWhiteFalseForBlack = true)
            }
        } else {
            Log.d("NULL", "Null")
        }
    }

    fun restartTimerDialog(){
        _uiState.update { currentState ->
            currentState.copy(resetTimer = true)
        }
    }

    fun cancelRestartTimerDialog(){
        _uiState.update { currentState ->
            currentState.copy(resetTimer = false)
        }
    }

    /*White Timer Functions Below*/

    fun startWhiteTimer() {
        whiteCountdownTimer = object : CountDownTimer(
            whiteTimeInMilliSeconds,
            1000
        ) {
            override fun onFinish() {
                _uiState.update { currentState ->
                    currentState.copy(whiteTimeFinished = true)
                }
            }

            override fun onTick(p0: Long) {
                whiteTimeInMilliSeconds = p0
                updateWhiteTimeTextUi()
            }
        }

        _uiState.update { currentState ->
            currentState.copy(whiteTimerTextColor = Color.Red, whiteIsRunning = true)
        }
        whiteCountdownTimer.start()
    }

    fun pauseWhiteTimer (){
        _uiState.update { currentState ->
            currentState.copy(whiteTimerTextColor = Color.Black, whiteIsRunning = false)
        }
        whiteCountdownTimer.cancel()
    }

    fun updateWhiteTimeTextUi(){
        val minute = (whiteTimeInMilliSeconds / 1000) / 60
        val seconds = (whiteTimeInMilliSeconds / 1000) % 60
        _uiState.update { currentState ->
            currentState.copy(whiteTimerText = "$minute:$seconds")
        }
    }

    /* Black Timer Functions Below */

    fun startBlackTimer(){
        blackCountdownTimer = object : CountDownTimer(
            blackTimeInMilliSeconds,
            1000
        ){
            override fun onFinish() {
                _uiState.update { currentState ->
                    currentState.copy(blackTimeFinished = true)
                }
            }

            override fun onTick(p0: Long) {
                blackTimeInMilliSeconds = p0
                updateBlackTimeTextUi()
            }
        }

        _uiState.update { currentState ->
            currentState.copy(blackTimerTextColor = Color.Red, blackIsRunning = true)
        }
        blackCountdownTimer.start()
    }

    fun pauseBlackTimer(){
        _uiState.update { currentState ->
            currentState.copy(blackTimerTextColor = Color.White, blackIsRunning = false)
        }
        blackCountdownTimer.cancel()
    }

    fun updateBlackTimeTextUi(){
        val minute = (blackTimeInMilliSeconds / 1000) / 60
        val seconds = (blackTimeInMilliSeconds / 1000) % 60
        _uiState.update { currentState ->
            currentState.copy(blackTimerText = "$minute:$seconds")
        }
    }

    /* Change game mode functions below*/

    fun changeToRapidChessGameMode(){
        _uiState.update { currentState ->
            currentState.copy(gameModeTimeLength = 1500000L)
        }
        restartMatch()
    }

    fun changeToBlitzChessGameMode(){
        _uiState.update { currentState ->
            currentState.copy(gameModeTimeLength = 300000L)
        }
        restartMatch()
    }

    fun changeToBulletChessGameMode(){
        _uiState.update { currentState ->
            currentState.copy(gameModeTimeLength = 120000L)
        }
        restartMatch()
    }
}