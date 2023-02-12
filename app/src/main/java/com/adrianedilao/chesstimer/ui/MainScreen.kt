package com.adrianedilao.chesstimer.ui

import android.app.Activity
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adrianedilao.chesstimer.R
import com.adrianedilao.chesstimer.ui.theme.Stance
import com.adrianedilao.chesstimer.ui.theme.TimeFont
import com.adrianedilao.chesstimer.ui.theme.Vicious
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
    mediaPlayer: MediaPlayer,
    mediaPlayer2: MediaPlayer
) {
    val uiState by viewModel.uiState.collectAsState()

    Image(
        painter = painterResource(id = R.drawable.background1),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        BlackTime(
            uiState = uiState,
            timeText = uiState.blackTimerText,
            viewModel = viewModel,
            mediaPlayer = mediaPlayer
        )
        OptionBar(uiState = uiState, viewModel = viewModel, mediaPlayer = mediaPlayer)
        WhiteTime(
            uiState = uiState,
            timeText = uiState.whiteTimerText,
            viewModel = viewModel,
            mediaPlayer = mediaPlayer
        )

        if (uiState.whiteTimeFinished || uiState.blackTimeFinished) {
            mediaPlayer2.start()
            TimeFinishedDialog(viewModel = viewModel, uiState = uiState)
        }

        if (uiState.resetTimer) {
            ResetTimerDialog(viewModel = viewModel)
        }
    }
}

@Composable
fun BlackTime(
    modifier: Modifier = Modifier,
    uiState: MainState,
    timeText: String,
    viewModel: MainViewModel,
    mediaPlayer: MediaPlayer
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.45f)
            .clickable {
                mediaPlayer.start()
                viewModel.blackSwitchTime()
            },
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = timeText,
            fontSize = 80.sp,
            modifier = Modifier.rotate(180f),
            color = uiState.blackTimerTextColor,
            fontFamily = TimeFont
        )
    }
}

@Composable
fun WhiteTime(
    modifier: Modifier = Modifier,
    uiState: MainState,
    timeText: String,
    viewModel: MainViewModel,
    mediaPlayer: MediaPlayer
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(1f)
            .clickable {
                mediaPlayer.start()
                viewModel.whiteSwitchTime()
            },
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = timeText,
            fontSize = 80.sp,
            fontFamily = TimeFont,
            color = uiState.whiteTimerTextColor
        )
    }
}

@Composable
fun OptionBar(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    uiState: MainState,
    mediaPlayer: MediaPlayer
) {
    var showGameMode by remember { mutableStateOf(false) }

    val colorStops = arrayOf(
        0.0f to Vicious,
        1f to Stance
    )

    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.15f),
        elevation = 4.dp,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Brush.horizontalGradient(colorStops = colorStops))
        ) {
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        viewModel.restartTimerDialog()
                    },
                imageVector = Icons.Default.Refresh,
                contentDescription = "Restart Timer",
                tint = Color.White
            )

            Spacer(modifier = Modifier.width(50.dp))

            Icon(
                modifier = Modifier
                    .size(50.dp)
                    .clickable {
                        mediaPlayer.start()
                        viewModel.playOrPause()
                    },
                painter =
                if (!uiState.whiteIsRunning && !uiState.blackIsRunning) {
                    painterResource(id = R.drawable.ic_baseline_play_arrow_24)
                } else {
                    painterResource(id = R.drawable.ic_baseline_pause_24)
                },
                contentDescription = "Play or Pause Timer",
                tint = Color.White
            )

            Spacer(modifier = Modifier.width(50.dp))

            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { showGameMode = !showGameMode },
                painter = painterResource(id = R.drawable.ic_baseline_timer_24),
                contentDescription = "Change Timer",
                tint = Color.White
            )
            Row(modifier = Modifier.padding(start = 10.dp)) {
                DropdownMenu(
                    expanded = showGameMode,
                    onDismissRequest = { showGameMode = false }) {
                    DropdownMenuItem(onClick = { viewModel.changeToRapidChessGameMode() }) {
                        Text("Rapid Chess (25:00)")
                    }
                    DropdownMenuItem(onClick = { viewModel.changeToBlitzChessGameMode() }) {
                        Text("Blitz Chess (05:00)")
                    }
                    DropdownMenuItem(onClick = { viewModel.changeToBulletChessGameMode() }) {
                        Text("Bullet Chess (02:00)")
                    }
                }
            }

        }
    }
}

@Composable
private fun TimeFinishedDialog(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    uiState: MainState
) {
    val openDialog = remember { mutableStateOf(true) }
    val activity = (LocalContext.current as Activity)

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Non-clickable
            },
            title = { Text(text = "GAME OVER!") },
            text = {
                if (uiState.whiteTimeFinished) {
                    Text(text = "White ran out of time.")
                } else if (uiState.blackTimeFinished) {
                    Text(text = "Black ran out of time.")
                }
            },
            modifier = modifier,
            dismissButton = {
                TextButton(
                    onClick = {
                        activity.finish()
                    }
                ) {
                    Text(text = "DISMISS")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.restartMatch()
                        openDialog.value = false
                    }
                ) {
                    Text(text = "PLAY AGAIN")
                }
            }
        )
    }
}

@Composable
private fun ResetTimerDialog(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Non-clickable
            },
            title = { Text(text = "RESTART TIME") },
            text = { Text(text = "Want to reset timer?") },
            modifier = modifier,
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.cancelRestartTimerDialog()
                        openDialog.value = false
                    }
                ) {
                    Text(text = "CANCEL")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.restartMatch()
                        openDialog.value = false
                    }
                ) {
                    Text(text = "RESET")
                }
            }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
}