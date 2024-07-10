package com.example.messaging

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.civicengagementplatform.signIn.UserData
import com.example.messaging.Messaging.MessageViewModel
import com.example.messaging.signIn.GoogleAuthUiClient
import com.example.messaging.ui.theme.MessagingTheme
import com.example.messaging.user_profile.ProfilePicture
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d("FCM", token.toString())
        })
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            MessagingTheme {
                    MessagingApp(lifecycle = lifecycleScope)
            }
        }
    //    createNotificationChannel(this)
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagingApp(lifecycle: LifecycleCoroutineScope){
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route ?: ""
    val applicationContext = LocalContext.current
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    val userData: UserData? = googleAuthUiClient.signedInUser()
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    when(currentRoute) {
                        Screens.Contacts.name -> Text("Contacts")
                        Screens.Profile.name -> Text(text = "Profile")
                        "Message/{uname}/{id}" -> Text(text = contact_name)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Screens.Profile.name) }) {
                        userData?.let { ProfilePicture(it) } ?: R.drawable.no_image
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController)}
    ){
contentPadding ->
Column(modifier = Modifier.padding(contentPadding)) {
    allDestinations(lifecycleScope = lifecycle, navController)
}
    }

}