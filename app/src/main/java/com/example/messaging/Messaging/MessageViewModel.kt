package com.example.messaging.Messaging

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.messaging.User
import com.example.messaging.current_userId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class MessageViewModel: ViewModel() {


    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages
    val db = Firebase.firestore


    init {
        loadMessages()
    }

    fun sendMessage(senderId: String, receiverId: String, messageContent: String) {


        val messageId = UUID.randomUUID().toString()
        val timestamp = System.currentTimeMillis()

        val message = Message(messageId, senderId, receiverId, messageContent, timestamp)


        db.collection("messages")
            .document(messageId)
            .set(message)
            .addOnSuccessListener {
                // Message sent successfully
            }
            .addOnFailureListener { e ->
                // Handle error
                Log.w(TAG, "Error sending message", e)
            }
    }



    private fun loadMessages() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserId = currentUser?.uid ?: return

        // Query for messages where the current user is either the sender or receiver
        db.collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("LoadMessages", "Error fetching messages", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val messages = snapshot.documents.mapNotNull { document ->
                        val message = document.toObject(Message::class.java)
                        if (message != null && (message.senderId == currentUserId || message.receiverId == currentUserId ) && (message.receiverId == current_userId || message.senderId == current_userId)) {
                            message
                        } else {
                            null
                        }
                    }
                    _messages.value = messages
                }
            }
    }

}

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val messageContent: String = "",
    val timestamp: Long = 0L
)
