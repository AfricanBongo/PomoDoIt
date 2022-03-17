package com.mtabvuri.pomodoit.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mtabvuri.pomodoit.ui.theme.PomoDoItTheme

/**
 * This button emphasizes on a primary action.
 */
@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String = "Button",
    colors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = MaterialTheme.colors.secondary
    ),
    onClick: () -> Unit
) {
    Button(
        shape = RoundedCornerShape(25.dp),
        modifier = modifier,
        onClick = onClick,
        colors = colors
    ) {
        Text(text = text)
    }
}

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    text: String = "Button",
    colors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface
    ),
    onClick: () -> Unit
) {
    TextButton(onClick = onClick, colors = colors) {
        Text(text)
    }
}

@Preview
@Composable
fun PrimaryButtonPreview() {
    PomoDoItTheme {
        PrimaryButton {
            // Do Nothing
        }
    }
}

@Preview
@Composable
fun SecondaryButtonPreview() {
    PomoDoItTheme {
        SecondaryButton {
            // Do nothing
        }
    }
}