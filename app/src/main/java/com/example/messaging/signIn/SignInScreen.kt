package com.example.civicengagementplatform.signIn

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClickL: () -> Unit,
    function: () -> Unit
){
    val context=  LocalContext.current
    LaunchedEffect(key1 = state.signInError){
        state.signInError?.let{ error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    Column(modifier= Modifier) {
        Box(modifier= Modifier.fillMaxWidth()){
            Button(onClick =  onSignInClickL ) {
                Text(text = "Sign In With google")
            }
        }
    }

}

@Preview
@Composable
fun PreviewSignIn()
{
    SignInScreen(state = SignInState(), onSignInClickL = { /*TODO*/ }) {
        
    }
}