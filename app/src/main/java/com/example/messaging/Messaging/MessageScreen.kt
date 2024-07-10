package com.example.civicengagementplatform.ui.Messaging

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.messaging.Messaging.MessageViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.auth.FirebaseAuth
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

var unreadMessageCount = 0
@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@Composable
fun MessageScreen(
    name:String,
    id: String,
    messageViewModel: MessageViewModel = viewModel()
) {

    val listState = rememberLazyListState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        var message by remember { mutableStateOf("") }
        val currentUser = FirebaseAuth.getInstance().currentUser
        val messages = messageViewModel.messages.collectAsState()


        LaunchedEffect(messages.value.size) {
            if (messages.value.isNotEmpty()) {
                listState.animateScrollToItem(messages.value.size - 1)
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            items(messages.value) { messageItem ->
                val isCurrentUser = currentUser?.uid == messageItem.senderId
                val ZoneId = ZoneId.systemDefault()
                val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(messageItem.timestamp), ZoneId)
                val formatter = DateTimeFormatter.ofPattern("hh:mm")
                val time = localDateTime.format(formatter)


                if(messageItem.receiverId==currentUser?.uid && id == messageItem.senderId) {
                    messageViewModel.markMessageAsRead(messageItem.messageId)
                }




                val alignment = if (isCurrentUser) Arrangement.End else Arrangement.Start
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = alignment) {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .border(
                                1.dp,
                                if(isCurrentUser)MaterialTheme.colorScheme.secondary else
                                    MaterialTheme.colorScheme.tertiary,
                                RoundedCornerShape(
                                    if (isCurrentUser) 7.dp else 50.dp,
                                    7.dp,
                                    if (isCurrentUser) 50.dp else 7.dp,
                                    7.dp
                                )
                            ),
                        shape =  RoundedCornerShape(
                            if (isCurrentUser) 7.dp else 50.dp,
                            7.dp,
                            if (isCurrentUser) 50.dp else 7.dp,
                            7.dp
                        )
                    ) {
                        Text(
                            text = time,

                            modifier = Modifier.align(if(isCurrentUser)Alignment.Start else Alignment.End)
                                .padding(start = 4.dp, end = 4.dp),
                            fontSize = 12.sp
                        )
                        Text(
                            text = messageItem.messageContent,
                            fontWeight = FontWeight(600),
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp, bottom = 12.dp)
                                .align(Alignment.Start),
                            fontSize = 16.sp,

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
                shape = RoundedCornerShape(20.dp),
                label = { Text(text = "Type a message to $name") },
                maxLines = 2,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                modifier = Modifier
                    .border(1.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(20.dp))
                    .padding(end = 4.dp)
                    .weight(1f)
            )
           Icon(
               imageVector = Icons.AutoMirrored.Filled.Send,
               contentDescription = "send",
               tint = Color(0xFF812A03),
               modifier = Modifier
                   .size(35.dp)
                   .align(Alignment.CenterVertically)
                   .clickable {
                       if (message.isNotBlank() && currentUser != null) {
                           currentUser.displayName?.let {
                               messageViewModel.sendMessage(
                                   messageContent = message,
                                   senderId = currentUser.uid,
                                   senderName = it,
                                   receiverId = id,
                                   isRead = false
                               )
                           }
                           message = ""
                       }
                   }
           )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun MessagePrew(){
    MessageScreen(name = "Name", id = "sss")
}