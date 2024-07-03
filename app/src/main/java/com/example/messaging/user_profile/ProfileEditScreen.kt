package com.example.civicengagementplatform.ui.user_profile

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chat_appication.R
import coil.compose.AsyncImage
import com.example.civicengagementplatform.signIn.UserData


@SuppressLint("SuspiciousIndentation")
@Composable
fun ProfileScreen(
    userData: UserData?,
    onSignOut: () ->Unit,
    onClick: () ->Unit
) {
//    val applicationContext = LocalContext.current
//    val googleAuthUiClient by lazy {
//        GoogleAuthUiClient(
//            context = applicationContext,
//            oneTapClient = Identity.getSignInClient(applicationContext)
//        )
//    }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            if (userData != null) {
                ProfilePicture(userData)
            }
            Spacer(modifier = Modifier.padding(20.dp))

            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
            if (userData != null) {
                Text(
                    text = ("Name: " + userData.userName),
                    modifier = Modifier
                        .padding(12.dp),
                    fontWeight = FontWeight(800)
                )
            }

            if (userData != null) {
                userData.email?.let {
                    Text(
                            text = "Email: $it",
                        modifier = Modifier
                            .padding( 12.dp),
                        fontWeight = FontWeight(800)
                    )
                }
            }
        }
           
                Button(onClick =  if(userData!=null) {onSignOut} else{onClick}  , modifier =Modifier.fillMaxWidth()) {
                    Text(text = if(userData!=null) {"Log out"} else{"Log in"})
                }
    }
}
@Composable
fun ProfilePicture(userData:UserData) {

    if (userData.profilePictureUrl != null) {
        AsyncImage(
            model = userData.profilePictureUrl, contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .wrapContentWidth()
                .clip(CircleShape),
            contentScale = ContentScale.FillWidth
        )
    } else {
        Image(
            painter = painterResource(R.drawable.no_image),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
        )
    }
}

@Preview
@Composable
fun PreviewProfile(){
    ProfileScreen(userData = UserData("","Ahmad Yousafzai","","ahmadyousafzai@gmail.com"), onSignOut = { /*TODO*/ }) {
        
    }
}