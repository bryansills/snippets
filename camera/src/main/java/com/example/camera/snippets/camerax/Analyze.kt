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

import android.util.Size
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.Executor

fun ComponentActivity.bindImageAnalysis(
    executor: Executor,
    cameraProvider: ProcessCameraProvider,
    cameraSelector: CameraSelector,
    preview: Preview
) {
    // [START android_camerax_analyze_bind]
    val resolutionStrategy = ResolutionStrategy(
        Size(1280, 720),
        ResolutionStrategy.FALLBACK_RULE_CLOSEST_LOWER
    )
    val resolutionSelector = ResolutionSelector.Builder()
        .setResolutionStrategy(resolutionStrategy)
        .build()

    val imageAnalysis = ImageAnalysis.Builder()
        // enable the following line if RGBA output is needed.
        // .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
        .setResolutionSelector(resolutionSelector)
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()
    // @@@ Bryan used trailing lambda syntax here @@@
    imageAnalysis.setAnalyzer(executor) { imageProxy ->
        val rotationDegrees = imageProxy.imageInfo.rotationDegrees
        // insert your code here.
        // @@@ Bryan: just have "..." here @@@
        // after done, release the ImageProxy object
        imageProxy.close()
    }

    cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, imageAnalysis, preview)
    // [END android_camerax_analyze_bind]
}

/**
 * Suggested prose updates:
 * 1) For the bullet point starting with "Resolution and AspectRatio: You can set either...", update
 * the text to:
 *
 * Resolution or aspect ratio:  You can specify a set of requirements and priorities used to select
 * a resolution through [setResolutionSelector()](add link here). For information about the
 * resolution matching algorithm, see the documentation for [ResolutionSelector](add link here).
 *
 * 2) Remove the entire paragraph starting with "Applications can set either the resolution..."
 */
