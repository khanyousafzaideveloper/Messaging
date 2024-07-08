package com.example.messaging

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(){
    var email by rememberSaveable {
        mutableStateOf("")
    }
    Row(modifier= Modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Email: ", modifier= Modifier.padding(4.dp))
        TextField(value = email, onValueChange = {email= it})
    }

}