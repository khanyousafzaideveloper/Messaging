package com.example.civicengagementplatform.ui.Messaging

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(
    messageViewModel: MessageViewModel = viewModel()
) {
    Column (modifier= Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top){

        var message by remember { mutableStateOf(messageViewModel.message) }


        LazyColumn(modifier = Modifier
            .weight(1f)
            .fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
            items(messageViewModel.texts) { messages ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Card(
                        modifier = Modifier
                            .padding(end = 16.dp, bottom = 16.dp, top = 16.dp, start = 40.dp)
                            .border(
                                3.dp,
                                MaterialTheme.colorScheme.secondary,
                                RoundedCornerShape(7.dp, 7.dp, 50.dp, 7.dp)
                            )
                    ) {

                        Text(
                            text = messages,
                            fontWeight = FontWeight(500),
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.Start)
                        )
                    }
                }
//                Card(
//                    modifier = Modifier
//                        .padding(16.dp, end = 40.dp)
//                        .border(3.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(7.dp, 7.dp, 7.dp, 40.dp))
//                ) {
//                    Text(text = messages, fontWeight = FontWeight(500), modifier = Modifier
//                        .padding(16.dp)
//                    )
//                }
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
                label = { Text(text = "Type a message") },
                maxLines = 2,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .weight(1f)
                    .border(2.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(20.dp))
            )
            Button(onClick = { messageViewModel.OnSend() }) {
                Text(text = "Send")
            }
        }
    }
}

@Preview
@Composable
fun messagingPreview(){
    MessageScreen()
}