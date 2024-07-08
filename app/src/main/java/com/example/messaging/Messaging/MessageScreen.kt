package com.example.civicengagementplatform.ui.Messaging

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.messaging.Messaging.MessageViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@SuppressLint("UnrememberedMutableState")
@Composable
fun MessageScreen(
    name:String,
    id: String,
    messageViewModel: MessageViewModel = viewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        var message by remember { mutableStateOf("") }
        val currentUser = FirebaseAuth.getInstance().currentUser
        val messages = messageViewModel.messages.collectAsState()

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            items(messages.value) { messageItem ->
                val isCurrentUser = currentUser?.uid == messageItem.senderId
                val alignment = if (isCurrentUser) Arrangement.End else Arrangement.Start
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = alignment) {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .border(
                                3.dp,
                                MaterialTheme.colorScheme.secondary,
                                RoundedCornerShape(
                                    if (isCurrentUser) 7.dp else 50.dp,
                                    7.dp,
                                    if (isCurrentUser) 50.dp else 7.dp,
                                    7.dp
                                )
                            )
                    ) {
                        Text(
                            text = messageItem.messageContent,
                            fontWeight = FontWeight(500),
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.Start)
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                value = message,
                onValueChange = { message = it },
                label = { Text(text = "Type a message to $name") },
                maxLines = 2,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .weight(1f)
                    .border(2.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(20.dp))
            )
            Button(onClick = {
                if (message.isNotBlank() && currentUser != null) {
                    messageViewModel.sendMessage(
                        messageContent = message,
                        senderId = currentUser.uid,
                        receiverId = id
                    )
                    message = ""
                }
            }) {
                Text(text = "Send")
            }
        }
    }
}