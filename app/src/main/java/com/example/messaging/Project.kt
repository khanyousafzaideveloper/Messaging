package com.example.messaging


import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import android.app.Activity.RESULT_OK
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.messaging.signIn.GoogleAuthUiClient
import com.example.civicengagementplatform.signIn.SignInScreen
import com.example.civicengagementplatform.signIn.SignInViewModel
import com.example.messaging.user_profile.ProfileScreen
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch

@Composable
fun allDestinations(lifecycleScope: LifecycleCoroutineScope) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val googleAuthUiClient by remember {
        mutableStateOf(GoogleAuthUiClient(context, Identity.getSignInClient(context)))
    }

    // Initialize Firebase
    LaunchedEffect(Unit) {
        FirebaseApp.initializeApp(context)
    }

    NavHost(navController, startDestination = Screens.Profile.name) {
        composable(Screens.SignInMethods.name) {
            SignInMethods(
                onSignOutGoogle = {
                    lifecycleScope.launch {
                        googleAuthUiClient.signout()
                        Toast.makeText(context, "Signed Out", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                }
            ) {
                // Additional UI for SignInMethods
            }
        }

        composable(Screens.Profile.name) {
            ProfileScreen(
                userData = googleAuthUiClient.signedInUser(),
                onSignOut = {
                    lifecycleScope.launch {
                        googleAuthUiClient.signout()
                        Toast.makeText(context, "Signed Out", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                },
                onClick = {
                    navController.navigate(Screens.SignInGoogle.name)
                }
            )
        }

        composable(Screens.SignInGoogle.name) {
            val viewModel2 = viewModel<SignInViewModel>()
            val state by viewModel2.state.collectAsState()

            LaunchedEffect(key1 = Unit){
                if(googleAuthUiClient.signedInUser()!=null){
                    navController.navigate(Screens.Profile.name)
                }
            }

            val launcher= rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if(result.resultCode == RESULT_OK){
                        lifecycleScope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel2.onSignInResult(signInResult)
                        }
                    }
                }
            )
            LaunchedEffect(key1 = state.isSignInSuccessful){
                if(state.isSignInSuccessful){
                    Toast.makeText(
                        context,
                        "Sign in Successful",
                        Toast.LENGTH_SHORT
                    ).show()

                    navController.navigate(Screens.Profile.name)
                    viewModel2.resetState()
                }
            }

            SignInScreen(
                state = state,
                onSignInClickL = {
                    lifecycleScope.launch {
                        val signInIntentSender= googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
            ) {

            }
        }
    }
}
enum class Screens {
    SignInMethods,
    SignInGoogle,
    Profile
}
