package com.mtabvuri.pomodoit.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mtabvuri.pomodoit.R
import com.mtabvuri.pomodoit.ui.components.PrimaryButton
import com.mtabvuri.pomodoit.ui.theme.PomoDoItTheme
import com.mtabvuri.pomodoit.ui.theme.PrimaryMediumEmphasis
import com.mtabvuri.pomodoit.ui.theme.Progress

@Composable
fun HomeScreen() {
    ProgressGroup(
        progress = 0.95f,
        modifier = Modifier.padding(4.dp)
    )
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
    pomodoroState: PomodoroState
) {
    val stringResourceId = when (pomodoroState) {
        is PomodoroState.Default -> {
            if (pomodoroState.completedPomodoros == 4) {
                R.string.start_another_btn
            } else {
                R.string.start_btn
            }
        }
        else -> R.string.start_btn
    }

    PrimaryButton(
        modifier = modifier,
        text = stringResource(stringResourceId),
        onClick = when (pomodoroState) {
            is PomodoroState.Running -> pomodoroState.onWait
            is PomodoroState.Default -> pomodoroState.onStart
            else -> {return Unit}
        }
    )
}

@Preview(heightDp = 800, widthDp = 320, showBackground = true)
@Composable
fun HomeScreenPreview() {
    PomoDoItTheme {
        HomeScreen()
    }
}