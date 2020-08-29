/**
 * MIT License
 * <p>
 * Copyright (C) 2020 by Qifan YANG (@underwindfall)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.qifan.emojibattle.engine

import android.graphics.Bitmap
import com.coloros.ocs.base.task.TaskExecutors
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.qifan.emojibattle.extension.debug
import com.qifan.emojibattle.internal.ScopedExecutor
import com.qifan.emojibattle.internal.sdk.VideoRawData
import com.qifan.emojibattle.model.FrameFace

private const val SMILE_THRESHOLD = 0.15
private const val EYE_OPEN_THRESHOLD = 0.5

class DetectionEngine constructor(
    private val videoRawData: VideoRawData
) : VideoRawData.VideoObserver {
    private val executor by lazy { ScopedExecutor(TaskExecutors.MAIN_THREAD) }
    private var detector: FaceDetector? = null
    private var detectionListener: DetectionListener? = null

    fun initialize(detectionListener: DetectionListener) {
        this.detectionListener = detectionListener
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()
        detector = FaceDetection.getClient(highAccuracyOpts)
        videoRawData.subscribe(this)
    }

    private fun processImage(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        detector?.process(image)
            ?.addOnSuccessListener(executor) { faces ->
                faces.forEach { face: Face ->
                    debug("detector process $face")
                    val smiling = face.smilingProbability ?: 0f > SMILE_THRESHOLD
                    val leftEyeClosed = face.leftEyeOpenProbability ?: 0f < EYE_OPEN_THRESHOLD
                    val rightEyeClosed = face.rightEyeOpenProbability ?: 0f < EYE_OPEN_THRESHOLD
                    val frameFace: FrameFace = if (smiling) {
                        when {
                            leftEyeClosed -> FrameFace.LEFT_WINK
                            rightEyeClosed -> FrameFace.RIGHT_WINK
                            else -> FrameFace.SMILE
                        }
                    } else {
                        FrameFace.UNKNOWN
                    }
                    detectionListener?.onSuccess(frameFace)
                }
            }
            ?.addOnFailureListener(executor) { e ->
                detectionListener?.onFail(e)
            }
    }

    fun shutdown() {
        videoRawData.unsubscribe()
        executor.shutdown()
        detector?.close()
        detectionListener = null
        detector = null
    }

    override fun onCaptureVideoFrame(bitmap: Bitmap) {
        processImage(bitmap)
    }

    interface DetectionListener {
        fun onSuccess(face: FrameFace)
        fun onFail(e: Exception)
    }
}
