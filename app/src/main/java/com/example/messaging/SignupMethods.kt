package com.example.chat_appication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SignInMethods(
    onSignOutGoogle: () ->Unit,
    onSignInGoogle: () ->Unit
){
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Button(onClick = { /*TODO*/ },
            modifier=Modifier.fillMaxWidth()
        ) {
            Text(text = "Continue with Google")
        }
        Button(onClick = { /*TODO*/ },
            modifier=Modifier.fillMaxWidth()
        ) {
            Text(text = "Continue with Email")
        }
        Button(onClick = { /*TODO*/ },
            modifier=Modifier.fillMaxWidth()
        ) {
            Text(text = "Continue with Phone")
        }
    }
}

@Preview
@Composable
fun PreviewSignUp(){
    SignInMethods(onSignInGoogle = {}, onSignOutGoogle = {})
}