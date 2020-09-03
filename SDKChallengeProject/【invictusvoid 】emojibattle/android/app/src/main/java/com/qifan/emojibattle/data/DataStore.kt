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
package com.qifan.emojibattle.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.functions.FirebaseFunctions
import com.qifan.emojibattle.extension.debug
import com.qifan.emojibattle.extension.warn
import com.qifan.emojibattle.model.GameSessionResult

class DataStore {
  private val db by lazy { FirebaseFirestore.getInstance() }
  private val functions by lazy { FirebaseFunctions.getInstance() }
  fun setResult(gameSessionResult: GameSessionResult, callback: () -> Unit) {
    db.collection(gameSessionResult.roomId)
      .document(gameSessionResult.userId)
      .set(gameSessionResult, SetOptions.merge())
      .addOnSuccessListener { callback() }
      .addOnFailureListener { e -> warn("firestore set data $e") }
  }

  fun getWinner(gameSessionResult: GameSessionResult, callback: (Boolean) -> Unit) {
    db.collection(gameSessionResult.roomId)
      .orderBy("verifiedTimes")
      .limit(2)
      .get()
      .addOnSuccessListener { snapshot ->
        val userId = snapshot.documents.first().get("userId")
        callback(userId == gameSessionResult.userId)
      }
  }

  fun delete(roomId: String) {
    functions.getHttpsCallable("deleteRoomInfo")
      .call(mapOf("path" to roomId))
      .addOnSuccessListener { debug("delete successfully") }
  }
}
