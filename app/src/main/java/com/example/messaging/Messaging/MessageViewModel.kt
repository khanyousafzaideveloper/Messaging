package com.example.messaging.Messaging

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MessageViewModel: ViewModel() {
    var message by  mutableStateOf("")
    var texts by mutableStateOf(listOf<String>())
    val db = Firebase.firestore


    fun OnSend() {
        if (message.isNotBlank()) {
            texts = texts + message
        }

        val user = hashMapOf(
            "message" to message,
        )

        db.collection("messages")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")

            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
            message = ""
    }
}