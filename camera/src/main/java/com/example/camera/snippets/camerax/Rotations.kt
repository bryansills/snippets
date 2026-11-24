/*
 * Copyright 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.camera.snippets.camerax

import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Build
import android.view.OrientationEventListener
import android.view.Surface
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.lifecycle.compose.LifecycleStartEffect

// [START android_camerax_rotation_orientationeventlistener]
@Composable
private fun RotationOrientationEventListener(
    onRotation: (Int) -> Unit,
    context: Context = LocalContext.current
) {
    val onTargetRotation by rememberUpdatedState(onRotation)
    LifecycleStartEffect(context) {
        val orientationEventListener = object : OrientationEventListener(context) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }

                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }

                onTargetRotation(rotation)
            }
        }

        orientationEventListener.enable()

        onStopOrDispose {
            orientationEventListener.disable()
        }
    }
}
// [END android_camerax_rotation_orientationeventlistener]

// [START android_camerax_rotation_displaylistener]
@Composable
private fun RotationDisplayListener(
    onRotation: (Int) -> Unit,
    context: Context = LocalContext.current,
    inspection: Boolean = LocalInspectionMode.current
) {
    val onTargetRotation by rememberUpdatedState(onRotation)

    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    var currentRotation by remember {
        val initialRotation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.display.rotation
        } else {
            windowManager.defaultDisplay.rotation
        }
        mutableIntStateOf(initialRotation)
    }

    LifecycleStartEffect(context) {
        if (inspection) return@LifecycleStartEffect onStopOrDispose { }

        val displayManager = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val displayListener = object : DisplayManager.DisplayListener {
            override fun onDisplayChanged(displayId: Int) {
                val newRotation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    context.display.rotation
                } else {
                    windowManager.defaultDisplay.rotation
                }

                if (newRotation != currentRotation) {
                    currentRotation = newRotation
                    onTargetRotation(newRotation)
                }
            }
            override fun onDisplayAdded(displayId: Int) = Unit
            override fun onDisplayRemoved(displayId: Int) = Unit
        }
        displayManager.registerDisplayListener(displayListener, null)
        onStopOrDispose {
            displayManager.unregisterDisplayListener(displayListener)
        }
    }
}
// [END android_camerax_rotation_displaylistener]