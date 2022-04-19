package com.mtabvuri.pomodoit.ui.home

import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.mtabvuri.pomodoit.model.preferences.UserPreferencesRepository
import com.mtabvuri.pomodoit.nav.PortraitLayout
import com.mtabvuri.pomodoit.ui.components.PrimaryButton
import com.mtabvuri.pomodoit.ui.settingsIcon
import com.mtabvuri.pomodoit.ui.theme.OnPrimaryMediumEmphasis
import com.mtabvuri.pomodoit.ui.theme.PomoDoItTheme
import com.mtabvuri.pomodoit.ui.theme.Progress

private const val TIME_TEXT_CONSTRAINT = "time_text"
private const val SETTINGS_CONSTRAINT = "settings"
private const val BUTTON_BOX_CONSTRAINT = "button_box"

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = HomeScreenViewModel(
        UserPreferencesRepository(LocalContext.current)
    ),
    onSettingsButtonClicked: () -> Unit
) {
    PortraitLayout {
        ConstraintLayout(
            constraintSet = homeScreenConstraints(),
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
                .padding(all = 16.dp)
        ) {
            IconButton(
                onClick = onSettingsButtonClicked,
                modifier = Modifier.layoutId(SETTINGS_CONSTRAINT)
            ) {
                Icon(
                    imageVector = settingsIcon.imageVector,
                    contentDescription = stringResource(settingsIcon.contentDescription),
                    tint = MaterialTheme.colors.primary,
                )
            }

            SessionTimeView(
                state = PomodoroState.WAITING,
                timeInMillis = viewModel.timeInMillis,
                modifier = Modifier.layoutId(TIME_TEXT_CONSTRAINT)
            )

            StartPomodoroButton(
                onClick = viewModel::startTimer,
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(48.dp)
                    .layoutId(BUTTON_BOX_CONSTRAINT),
            )
        }
    }
}

private fun homeScreenConstraints(): ConstraintSet {
    return ConstraintSet {
        val timeText = createRefFor(TIME_TEXT_CONSTRAINT)
        val settingsButton = createRefFor(SETTINGS_CONSTRAINT)
        val buttonBox = createRefFor(BUTTON_BOX_CONSTRAINT)
        
        constrain(settingsButton) {
            top.linkTo(parent.top)
            end.linkTo(parent.end)
        }
        
        constrain(timeText) {
            top.linkTo(settingsButton.bottom, 120.dp)
            centerHorizontallyTo(parent)
        }
        
        constrain(buttonBox) {
            top.linkTo(timeText.bottom, 60.dp)
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
                color = OnPrimaryMediumEmphasis
            )
        )

        Text(
            text = timeAsString,
            style = MaterialTheme.typography.h1.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
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
    modifier: Modifier = Modifier,
    pomodoroState: PomodoroState
) {

}

@Composable
private fun ProgressGroup(
    progress: Float,
    modifier: Modifier = Modifier
) {

//    TODO
//    val progressPillModifier = Modifier
//        .clip(MaterialTheme.shapes.small)
//        .background(PrimaryMediumEmphasis)
//        .requiredWidth(40.dp)
//        .requiredHeight(24.dp)
//
//    val filledPills: Int = progress.coerceIn(0f, 1f).div(0.25f).toInt()
//    var individualProgress: Float
//    Row(
//        modifier = modifier,
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//       for (i in 1..filledPills) {
//           individualProgress = if (filledPills > i) 1f
//           else (progress % 0.25f) * 4
//
//           ProgressPill(progress = individualProgress, modifier = progressPillModifier)
//       }
//    }
}

@Composable
private fun ProgressPill(
    progress: Float,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        var filledProgress = progress.coerceIn(0f, 1f)
        filledProgress = (filledProgress / 1f) * maxWidth.value

        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .requiredWidth(filledProgress.dp)
                .background(Progress)
        )

    }
}

@Composable
fun StartPomodoroButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    PrimaryButton(
        modifier = modifier,
        text = stringResource(R.string.start_btn),
        onClick = onClick
    )
}

@Preview
@Composable
fun SessionTimeViewPreview() {
    PomoDoItTheme {
        SessionTimeView(PomodoroState.WAITING, (10000 * 78.6).toLong())
    }
}

@Preview(heightDp = 800, widthDp = 320)
@Composable
fun HomeScreenPreview() {
    PomoDoItTheme {
        HomeScreen {}
    }
}