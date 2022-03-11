package com.mtabvuri.pomodoit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mtabvuri.pomodoit.ui.splash.SplashScreen
import com.mtabvuri.pomodoit.ui.theme.PomoDoItTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomoDoItTheme {
                EntryScreen()
            }
        }
    }
}

@Composable
fun EntryScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        SplashScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PomoDoItTheme {
        EntryScreen()
    }
}