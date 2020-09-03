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
package com.qifan.emojibattle.internal.sdk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class YUVUtils {

  public static void swapYU12toYUV420SP(
      byte[] yu12bytes, byte[] i420bytes, int height, int yStride) {
    System.arraycopy(yu12bytes, 0, i420bytes, 0, yStride * height);
    int startPos = yStride * height;
    int yv_start_pos_u = startPos;
    int yv_start_pos_v = startPos + startPos / 4;
    for (int i = 0; i < startPos / 4; i++) {
      i420bytes[startPos + 2 * i + 0] = yu12bytes[yv_start_pos_v + i];
      i420bytes[startPos + 2 * i + 1] = yu12bytes[yv_start_pos_u + i];
    }
  }

  public static Bitmap i420ToBitmap(
      int width, int height, int rotation, int bufferLength, byte[] buffer, int yStride) {
    byte[] NV21 = new byte[bufferLength];
    swapYU12toYUV420SP(buffer, NV21, height, yStride);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    int[] strides = {yStride, yStride};
    YuvImage image = new YuvImage(NV21, ImageFormat.NV21, width, height, strides);

    image.compressToJpeg(new Rect(0, 0, image.getWidth(), image.getHeight()), 100, baos);

    // rotate picture when saving to file
    Matrix matrix = new Matrix();
    matrix.postRotate(rotation);
    byte[] bytes = baos.toByteArray();
    try {
      baos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
  }
}
