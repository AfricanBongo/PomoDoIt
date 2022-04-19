package com.mtabvuri.pomodoit.nav

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.mtabvuri.pomodoit.nav.NavScreen.*
import com.mtabvuri.pomodoit.ui.home.HomeScreen
import com.mtabvuri.pomodoit.ui.preferences.PreferencesScreen
import com.mtabvuri.pomodoit.ui.settings.SettingsScreen
import com.mtabvuri.pomodoit.ui.splash.SplashScreen

/**
 * A navigation destination within the app.
 */
enum class NavScreen {
    Splash, Preferences, Home, Settings
}

/**
 * Navigation for the entire app.
 * @param navController An AnimatedNavHostController.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PomoDoItNavHost(navController: NavHostController) {
    AnimatedNavHost(
        navController = navController, startDestination = Splash.name
    ) {
        composable(route = Splash.name) { SplashScreen {
            navController.navigate(Preferences.name)
        }}
        composable(
            Preferences.name,
            enterTransition = {
                when (initialState.destination.route) {
                    Splash.name ->
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Up)
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Home.name ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Left)
                    else -> null
                }
            }
        ) {
            PreferencesScreen {
                navController.navigate(Home.name)
            }
        }

        composable(
            Home.name,
            enterTransition = {
                when (initialState.destination.route) {
                    Preferences.name ->
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Left)
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Settings.name ->
                        slideOutOfContainer(AnimatedContentScope.SlideDirection.Up)
                    else -> null
                }
            }
        ) {
            HomeScreen {
                navController.navigate(Settings.name)
            }
        }

        composable(Settings.name) { SettingsScreen {
            navController.popBackStack()
        }}
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

/**
 * Used to display fullscreen content that should be viewed in portrait mode only
 */
@Composable
fun PortraitLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    Surface(modifier.fillMaxSize()) {
        content()
    }

}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}