package com.mtabvuri.pomodoit.nav

import android.content.pm.ActivityInfo
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mtabvuri.pomodoit.nav.NavScreen.*
import com.mtabvuri.pomodoit.ui.preferences.PreferencesScreen
import com.mtabvuri.pomodoit.ui.splash.SplashScreen

@Composable
fun PomoDoItNavHost(navController: NavHostController) {
    NavHost(
        navController = navController, startDestination = Splash.name
    ) {
        composable(Splash.name) { SplashScreen {
            navController.navigate(Preferences.name)
        }}
        composable(Preferences.name) { PreferencesScreen() }
    }
}


/**
 * Lock the screen within the given orientation.
 * @param orientation An orientation value of [ActivityInfo].
 */
@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            // restore original orientation when view disappears
            activity.requestedOrientation = originalOrientation
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}