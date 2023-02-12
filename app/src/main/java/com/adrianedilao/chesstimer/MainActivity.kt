package com.adrianedilao.chesstimer

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.adrianedilao.chesstimer.ui.MainScreen
import com.adrianedilao.chesstimer.ui.theme.ChessTimerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mediaPlayer = MediaPlayer.create(this, R.raw.click)
        val mediaPlayer2 = MediaPlayer.create(this, R.raw.buzzer)

        setContent {
            ChessTimerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen(mediaPlayer = mediaPlayer, mediaPlayer2 = mediaPlayer2)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
}