package com.mtabvuri.pomodoit.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mtabvuri.pomodoit.R

data class ImageResource(@DrawableRes val drawableId: Int, @StringRes val stringId: Int)

val fullLogo = ImageResource(R.drawable.ic_full_logo, R.string.logo_description)