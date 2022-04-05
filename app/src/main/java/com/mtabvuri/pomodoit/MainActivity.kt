package com.mtabvuri.pomodoit

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.mtabvuri.pomodoit.nav.PomoDoItNavHost
import com.mtabvuri.pomodoit.ui.theme.PomoDoItTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomoDoItApp(window)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PomoDoItApp(window: Window?) {
    PomoDoItTheme {
        window?.statusBarColor = MaterialTheme.colors.primaryVariant.toArgb()
        val navController = rememberAnimatedNavController()
        PomoDoItNavHost(navController)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PomoDoItApp(null)
}