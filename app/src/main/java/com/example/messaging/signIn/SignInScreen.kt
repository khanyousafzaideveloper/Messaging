package com.example.civicengagementplatform.signIn

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messaging.R

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
            Button(
                shape = RectangleShape,
                onClick =  onSignInClickL ,
                modifier = Modifier
                    .padding(top = 16.dp, start = 8.dp, end = 8.dp)
                    .fillMaxWidth(),

            ) {
                Row (modifier = Modifier.fillMaxWidth()){
                    Image(
                        painter = painterResource(R.drawable.search),
                        contentDescription = "<a href=\"https://www.flaticon.com/free-icons/google\" title=\"google icons\">Google icons created by Freepik - Flaticon</a>",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(64.dp))
                    Text(
                        text = "Sign In With google",
                        fontSize = 15.sp,
                        fontWeight = FontWeight(600),
                        modifier = Modifier.fillMaxWidth()
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    )
                }

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