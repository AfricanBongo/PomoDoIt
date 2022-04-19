package com.mtabvuri.pomodoit.nav

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
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

    val context = LocalContext.current

    // Controls the onboarding process.
    val onboardingPreference by remember {
        mutableStateOf(OnboardingViewModel(context = context))
    }

    // Whether or not onboarding should be done.
    val shouldOnboard by onboardingPreference.shouldOnboard.collectAsState(initial = false)

    AnimatedNavHost(
        navController = navController, startDestination = Splash.name
    ) {
        composable(route = Splash.name) { SplashScreen {
            if (shouldOnboard) {
                navController.navigate(Preferences.name)
            } else {
                navController.navigate(Home.name)
            }
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
                onboardingPreference.cancelOnboarding(true)
                navController.navigate(
                    route = Home.name,
                    navOptions = navOptions { popUpTo(Splash.name) }
                )
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

