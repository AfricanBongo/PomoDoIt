package com.mtabvuri.pomodoit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.mtabvuri.pomodoit.nav.PomoDoItNavHost
import com.mtabvuri.pomodoit.ui.theme.PomoDoItTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomoDoItApp()
        }
    }
}

@Composable
fun PomoDoItApp() {
    PomoDoItTheme {
        val navController = rememberNavController()
        PomoDoItNavHost(navController)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PomoDoItApp()
}