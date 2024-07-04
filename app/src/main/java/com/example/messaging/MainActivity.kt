package com.example.messaging

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.messaging.ui.theme.MessagingTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            MessagingTheme {
                    MessagingApp(lifecycle = lifecycleScope)
            }
        }
    }
}

@Composable
fun MessagingApp(lifecycle: LifecycleCoroutineScope){
    val navController = rememberNavController()
    Scaffold (bottomBar = { BottomNavigationBar(navController = navController)}){
contentPadding ->
Column(modifier = Modifier.padding(contentPadding)) {
    allDestinations(lifecycleScope = lifecycle, navController)
}
    }

}