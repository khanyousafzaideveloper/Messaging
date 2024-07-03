package com.example.chat_appication

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import android.app.Activity.RESULT_OK
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.civicengagementplatform.signIn.GoogleAuthUiClient
import com.example.civicengagementplatform.signIn.SignInScreen
import com.example.civicengagementplatform.signIn.SignInViewModel
import com.example.civicengagementplatform.ui.user_profile.ProfileScreen
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch

@Composable
fun allDestinations(lifecycleScope: LifecycleCoroutineScope){
    val navController = rememberNavController()
    val applicationContext = LocalContext.current
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
   // FirebaseApp.initializeApp(applicationContext)
    NavHost(navController, startDestination = Screens.Profile.name ) {
        composable(Screens.SignInMethods.name) {
            SignInMethods(
                onSignOutGoogle = { /*TODO*/ }) {

            }

        }

        composable(Screens.Profile.name) {
            ProfileScreen(
                userData = googleAuthUiClient.signedInUser(), onSignOut = { lifecycleScope.launch {
                    googleAuthUiClient.signout()
                    Toast.makeText(
                        applicationContext,
                        "Signed Out",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.popBackStack()
                }
                },
                onClick = {navController.navigate(Screens.SignInGoogle.name)}
            )
        }
        composable(Screens.SignInGoogle.name){
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
                        applicationContext,
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



