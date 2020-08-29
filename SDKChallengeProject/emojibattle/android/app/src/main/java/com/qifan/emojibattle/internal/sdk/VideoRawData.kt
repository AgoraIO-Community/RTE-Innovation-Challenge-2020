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
package com.qifan.emojibattle.internal.sdk

import android.graphics.Bitmap
import java.nio.ByteBuffer

class VideoRawData {
    private val byteBufferCapture: ByteBuffer = ByteBuffer.allocateDirect(3240 * 1080) // default maximum video size Full HD+

    init {
        System.loadLibrary("apm-plugin-raw-data")
    }

    fun subscribe(observer: VideoObserver) {
        setVideoCaptureByteBuffer(byteBufferCapture)
        setCallback(object : RawDataCallback {
            override fun onCaptureVideoFrame(
                videoFrameType: Int,
                width: Int,
                height: Int,
                bufferLength: Int,
                yStride: Int,
                uStride: Int,
                vStride: Int,
                rotation: Int,
                renderTimeMs: Long
            ) {
                val buffer = ByteArray(bufferLength)
                byteBufferCapture.limit(bufferLength)
                byteBufferCapture.get(buffer)
                byteBufferCapture.flip()
                val bmp = YUVUtils.i420ToBitmap(
                    width,
                    height,
                    rotation,
                    bufferLength,
                    buffer,
                    yStride
                )
                observer.onCaptureVideoFrame(bmp)
                byteBufferCapture.put(buffer)
                byteBufferCapture.flip()
            }
        })
    }

    fun unsubscribe() {
        byteBufferCapture.clear()
        releasePoint()
    }

    private interface RawDataCallback {
        fun onCaptureVideoFrame(
            videoFrameType: Int,
            width: Int,
            height: Int,
            bufferLength: Int,
            yStride: Int,
            uStride: Int,
            vStride: Int,
            rotation: Int,
            renderTimeMs: Long
        )
    }

    interface VideoObserver {
        fun onCaptureVideoFrame(bitmap: Bitmap)
    }

    private external fun setCallback(callback: RawDataCallback)

    private external fun setVideoCaptureByteBuffer(byteBuffer: ByteBuffer)

    private external fun releasePoint()
}
