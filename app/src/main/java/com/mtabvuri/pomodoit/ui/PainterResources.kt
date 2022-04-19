package com.mtabvuri.pomodoit.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.mtabvuri.pomodoit.R

data class ImageResource(@DrawableRes val drawableId: Int, @StringRes val contentDescription: Int)
data class IconResource(val imageVector: ImageVector, @StringRes val contentDescription: Int)

val backIcon by lazy { IconResource(Icons.Outlined.ArrowBack, R.string.settings_back_description) }
val fullLogo by lazy { ImageResource(R.drawable.ic_full_logo, R.string.logo_description) }
val playIcon by lazy { IconResource(Icons.Outlined.PlayArrow, R.string.play_description) }
val selectedIcon by lazy { IconResource(Icons.Outlined.CheckCircle, R.string.selection_description) }
val settingsIcon by lazy { IconResource(Icons.Outlined.Settings, R.string.settings_description) }