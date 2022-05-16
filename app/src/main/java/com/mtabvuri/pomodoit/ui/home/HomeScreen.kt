package com.mtabvuri.pomodoit.ui.home

import android.text.format.DateFormat
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mtabvuri.pomodoit.R
import com.mtabvuri.pomodoit.model.notification.Sound
import com.mtabvuri.pomodoit.model.notification.Sounds
import com.mtabvuri.pomodoit.nav.PortraitLayout
import com.mtabvuri.pomodoit.ui.components.PrimaryButton
import com.mtabvuri.pomodoit.ui.components.SecondaryButton
import com.mtabvuri.pomodoit.ui.settingsIcon
import com.mtabvuri.pomodoit.ui.theme.*

private const val TIME_TEXT_CONSTRAINT = "time_text"
private const val SETTINGS_CONSTRAINT = "settings"
private const val BUTTON_BOX_CONSTRAINT = "button_box"
private const val PROGRESS_CONSTRAINT = "progress"

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = viewModel(
        factory = HomeScreenViewModelFactory(context = LocalContext.current)
    ),
    onSettingsButtonClicked: () -> Unit,
) {

    val tweenSpec = TweenSpec<Color>(
        durationMillis = 1000
    )

    // Change the background color when the state of the pomodoro session changes.
    val backgroundColor by animateColorAsState(
        targetValue = when (viewModel.pomodoroState) {
            is PomodoroState.Pomodoro -> {
                MaterialTheme.colors.secondary
            }
            else -> MaterialTheme.colors.background
        },
        animationSpec = tweenSpec
    )

    // Change the primary content color when the state of the pomodoro session changes.
    val primaryContentColor by animateColorAsState(
        targetValue = when (viewModel.pomodoroState) {
            is PomodoroState.Pomodoro -> {
                MaterialTheme.colors.onSecondary
            }
            else -> MaterialTheme.colors.onBackground
        },
        animationSpec = tweenSpec
    )

    // Change the secondary content color when the state of the pomodoro session changes.
    val secondaryContentColor by animateColorAsState(
        targetValue = when (viewModel.pomodoroState) {
            is PomodoroState.Pomodoro -> {
                OnSecondaryMediumEmphasis
            }
            else -> OnPrimaryMediumEmphasis
        },
        animationSpec = tweenSpec
    )

    PortraitLayout {
        ConstraintLayout(
            constraintSet = homeScreenConstraints(),
            modifier = Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
                .padding(vertical = 16.dp, horizontal = 24.dp)
        ) {
            IconButton(
                onClick = onSettingsButtonClicked,
                modifier = Modifier.layoutId(SETTINGS_CONSTRAINT)
            ) {
                Icon(
                    imageVector = settingsIcon.imageVector,
                    contentDescription = stringResource(settingsIcon.contentDescription),
                    tint = secondaryContentColor,
                )
            }

            SessionTimeView(
                state = viewModel.pomodoroState,
                colors = primaryContentColor to secondaryContentColor,
                timeInMillis = viewModel.timeInMillis,
                modifier = Modifier.layoutId(TIME_TEXT_CONSTRAINT)
            )

            ProgressGroup(
                completedSessions = viewModel.completedSessions,
                currentSessionProgress = viewModel.runningSessionCompletion,
                modifier = Modifier.layoutId(PROGRESS_CONSTRAINT)
            )

            ButtonBox(
                pomodoroState = viewModel.pomodoroState,
                primaryContentColor = primaryContentColor,
                backgroundColor = backgroundColor,
                secondaryContentColor = secondaryContentColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(56.dp)
                    .layoutId(BUTTON_BOX_CONSTRAINT)
            )

            SoundPlayer(
                pomodoroState = viewModel.pomodoroState,
                pomodoroSound = viewModel.pomodoroSound,
                breakSound = viewModel.breakSound,
                playSound = viewModel.playSound,
                onSoundPlayed = { viewModel.onSoundPlayed() }
            )
        }
    }
}

private fun homeScreenConstraints(): ConstraintSet {
    return ConstraintSet {
        val timeText = createRefFor(TIME_TEXT_CONSTRAINT)
        val settingsButton = createRefFor(SETTINGS_CONSTRAINT)
        val buttonBox = createRefFor(BUTTON_BOX_CONSTRAINT)
        val progressGroup = createRefFor(PROGRESS_CONSTRAINT)

        constrain(settingsButton) {
            top.linkTo(parent.top)
            end.linkTo(parent.end)
        }

        constrain(timeText) {
            top.linkTo(settingsButton.bottom, 120.dp)
            centerHorizontallyTo(parent)
        }

        constrain(progressGroup) {
            top.linkTo(timeText.bottom, 24.dp)
            centerHorizontallyTo(parent)
        }

        constrain(buttonBox) {
            top.linkTo(progressGroup.bottom, 60.dp)
            end.linkTo(parent.end)
            start.linkTo(parent.start)
        }
    }
}

@Composable
private fun SessionTimeView(
    state: PomodoroState,
    timeInMillis: Long,
    modifier: Modifier = Modifier,
    colors: Pair<Color, Color> = OnPrimaryMediumEmphasis to OnSecondaryMediumEmphasis, // First color is the primary color and second is secondary.
) {
    // Convert from long into minutes and seconds notation, i.e. 00:00
    val timeAsString: String = DateFormat.format("mm:ss", timeInMillis).toString()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = state.session,
            style = MaterialTheme.typography.subtitle1.copy(
                color = colors.second
            )
        )

        Text(
            text = timeAsString,
            style = MaterialTheme.typography.h1.copy(
                fontWeight = FontWeight.Bold,
                color = colors.first
            )
        )
    }
}

/*
A box that has different buttons for each stage of the pomodoro
i.e. pause and play pomodoro stage.
 */
@Composable
private fun ButtonBox(
    pomodoroState: PomodoroState,
    primaryContentColor: Color,
    secondaryContentColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
) {
    val startButton: @Composable (onClick: () -> Unit) -> Unit = { onClick ->
        StartButton(
            onClick = onClick,
            modifier = modifier,
        )
    }

    when (pomodoroState) {
        is PomodoroState.PomodoroWaiting -> {
            startButton(pomodoroState.onStart)
        }

        is PomodoroState.BreakWaiting -> {
            startButton(pomodoroState.onStart)
        }

        is PomodoroState.Pomodoro -> {
            PauseButton(
                modifier = modifier,
                onClick = pomodoroState.onPause,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = backgroundColor,
                    contentColor = primaryContentColor
                ))
        }

        is PomodoroState.ShortBreak -> {
            PauseButton(
                modifier = modifier,
                onClick = pomodoroState.onPause,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = backgroundColor,
                    contentColor = primaryContentColor
                ))
        }

        is PomodoroState.Pause -> {
            OnPauseButtonBox(
                stopButtonColors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = backgroundColor,
                    contentColor = secondaryContentColor
                ),
                resumeButtonColors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.secondary,
                    contentColor = MaterialTheme.colors.onSecondary
                ),
                onResume = pomodoroState.onResume,
                onStop = pomodoroState.onStop,
                modifier = modifier,
            )
        }
        else -> { /* Nothing */
        }
    }
}

@Composable
private fun SoundPlayer(
    pomodoroState: PomodoroState,
    pomodoroSound: Sound,
    breakSound: Sound,
    playSound: Boolean,
    onSoundPlayed: () -> Unit,
) {
    val soundToPlay: Sound = when (pomodoroState) {
        is PomodoroState.BreakWaiting -> breakSound
        is PomodoroState.Pomodoro -> pomodoroSound
        else -> Sound.NONE
    }

    if (playSound) {
        onSoundPlayed() // Stops sound from being restarted multiple times as recomposition occurs.
        Sounds.playSound(
            context = LocalContext.current,
            sound = soundToPlay,
        ) { /* Do nothing */ }
    }
}

@Composable
private fun ProgressGroup(
    completedSessions: Int,
    currentSessionProgress: Float,
    modifier: Modifier = Modifier,
) {
    val progressPillModifier = Modifier
        .clip(MaterialTheme.shapes.small)
        .requiredWidth(40.dp)
        .requiredHeight(24.dp)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        var pillProgress: Float

        for (i in 1..4) {
           pillProgress = when {
               i <= completedSessions -> 1f // Fill progress pill to indicate completed session.
               (i - completedSessions) == 1 -> currentSessionProgress// Fill to current progress after filling complete sessions.
               else -> 0f // When there are remaining sessions to be done.
           }

           ProgressPill(progress = pillProgress, modifier = progressPillModifier)
       }
    }
}

@Composable
private fun ProgressPill(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    LinearProgressIndicator(
        color = Progress,
        backgroundColor = PrimaryMediumEmphasis,
        progress = progress,
        modifier = modifier
    )
}

@Composable
private fun OnPauseButtonBox(
    resumeButtonColors: ButtonColors, // Should be outlined button colors
    stopButtonColors: ButtonColors, // Should be normal button colors
    modifier: Modifier = Modifier,
    onResume: () -> Unit,
    onStop: () -> Unit,
) {
    BoxWithConstraints(modifier = modifier) {
        // Each button should take up 49% of the width of the parent.
        val buttonWidth: Dp = maxWidth * 0.49f
        // Spacing between the buttons.
        val spaceWidth: Dp = maxWidth * 0.02f
        Row(horizontalArrangement = Arrangement.spacedBy(spaceWidth),
            modifier = Modifier.fillMaxSize()) {
            val borderColor: Color by stopButtonColors.contentColor(enabled = true)
            SecondaryButton(
                text = stringResource(R.string.stop_btn),
                border = BorderStroke(2.dp, borderColor),
                onClick = onStop,
                colors = stopButtonColors,
                modifier = Modifier
                    .requiredWidth(buttonWidth)
                    .fillMaxHeight()
            )
            PrimaryButton(
                text = stringResource(R.string.resume_btn),
                onClick = onResume,
                colors = resumeButtonColors,
                modifier = Modifier
                    .requiredWidth(buttonWidth)
                    .fillMaxHeight()
            )
        }
    }

}

@Composable
fun PauseButton(
    colors: ButtonColors,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val borderStrokeColor = colors.contentColor(enabled = true).value
    SecondaryButton(
        modifier = modifier,
        text = stringResource(R.string.pause_btn),
        onClick = onClick,
        colors = colors,
        border = BorderStroke(2.dp, borderStrokeColor)
    )
}

@Preview
@Composable
fun ProgressGroupPreview() {
    PomoDoItTheme {
        ProgressGroup(completedSessions = 1, currentSessionProgress = 0.98f)
    }
}
@Composable
fun StartButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    PrimaryButton(
        modifier = modifier,
        text = stringResource(R.string.start_btn),
        onClick = onClick,
    )
}

@Preview
@Composable
fun SessionTimeViewPreview() {
    PomoDoItTheme {
        SessionTimeView(PomodoroState.PomodoroWaiting { /* Nothing */ }, (10000 * 78.6).toLong())
    }
}

@Preview(heightDp = 800, widthDp = 320)
@Composable
fun HomeScreenPreview() {
    PomoDoItTheme {
        HomeScreen {}
    }
}