package com.example.messaging.Messaging

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import com.example.messaging.R
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

var new_Messages :Int = 0
class MessageViewModel: ViewModel() {


    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages
    val db = Firebase.firestore
    var sendNotification by mutableStateOf(false)
    private var previousMessages: List<Message> = emptyList()


    init {
        loadMessages()
    }

    fun sendMessage(senderId: String, senderName:String, receiverId: String, messageContent: String, isRead: Boolean) {


        val messageId = UUID.randomUUID().toString()
        val timestamp = System.currentTimeMillis()

        val message = Message(messageId, senderId, senderName, receiverId, messageContent, timestamp, isRead)


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
                    val newMessages = messages.filter { message ->
                        !previousMessages.contains(message)

                    }
                    // Update the previous messages

                    // Update LiveData
                    _messages.value = messages

                    // Handle new messages (e.g., notify user)
                    if (newMessages.isNotEmpty()) {
                        // Perform your action here, such as showing a notification

                        Log.d("LoadMessages", "New messages detected: ${newMessages.size}")
                        new_Messages = newMessages.size


                    }
                    _messages.value = messages
                }
            }
        }

    fun markMessageAsRead(messageId: String) {
        val messageRef = db.collection("messages").document(messageId)

        messageRef.update("read", true)
            .addOnSuccessListener {
                Log.d("Firestore", "Message marked as read!")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error updating document", e)
            }
    }




    fun getUnreadMessageCountForContact(contactId: String, currentUserId: String, callback: (Int) -> Unit) {
        db.collection("messages")
            .whereEqualTo("receiverId", currentUserId)
            .whereEqualTo("senderId", contactId)
            .whereEqualTo("read", false)
            .get()
            .addOnSuccessListener { querySnapshot ->
                Log.d("Firestoree",querySnapshot.size().toString())
                val unreadMessageCount = querySnapshot.size()
                if(unreadMessageCount>0){
                    sendNotification=true
                }
                callback(unreadMessageCount)


            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error getting documents", e)
                callback(0)
            }
    }


    fun sendNotification(context: Context, title: String, text: String) {

        val channelId = "default_channel"
        val notificationId = 1

        // Create a notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Default Channel for App"
            }
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.search) // replace with your notification icon
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // Display the notification
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(notificationId, notification)
        }
    }

}

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val senderName:String ="",
    val receiverId: String = "",
    val messageContent: String = "",
    val timestamp: Long = 0L,
    val isRead: Boolean = false
)
