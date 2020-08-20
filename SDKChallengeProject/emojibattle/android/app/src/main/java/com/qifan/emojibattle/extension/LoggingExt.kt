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
package com.qifan.emojibattle.extension

import android.util.Log

/**
 * Helper function to do the log debug work
 * @param tag simple java class name
 * @param message basic message
 * @param args additional message
 */
fun Any.debug(
    message: String,
    vararg args: Any?
) {
    try {
        Log.d(this::class.java.simpleName, message.format(*args))
    } catch (_: Exception) {
    }
}

/**
 * Helper function to do the log warn work
 * @param tag simple java class name
 * @param message basic message
 * @param args additional message
 */
fun Any.warn(
    message: String,
    vararg args: Any?
) {
    try {
        Log.w(this::class.java.simpleName, message.format(*args))
    } catch (_: Exception) {
    }
}

/**
 * Helper function to do the log error work
 * @param tag simple java class name
 * @param message basic message
 * @param args additional message
 */
fun Any.error(
    message: String,
    vararg args: Any?
) {
    try {
        Log.e(this::class.java.simpleName, message.format(*args))
    } catch (_: Exception) {
    }
}
