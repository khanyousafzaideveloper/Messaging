package com.example.messaging

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositionErrors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.civicengagementplatform.ui.Messaging.MessageScreen
import com.example.messaging.Messaging.MessageViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.messaging

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ContactScreen(navController: NavController){
    val viewModel: ContactListViewModel = viewModel()
    val contactListUiState by viewModel.contactListUiState
    val messageViewModel :MessageViewModel = viewModel()

    val context = LocalContext.current
    //val sendNotification by remember { mutableStateOf(messageViewModel.sendNotification) }
    LaunchedEffect(messageViewModel.sendNotification) {
        if (messageViewModel.sendNotification) {
            messageViewModel.sendNotification(context, messageViewModel.messages.value.last().senderName, messageViewModel.messages.value.last().messageContent)
            messageViewModel.sendNotification = false
        }
    }
    val showNotificationDialog = remember { mutableStateOf(false) }


    // Android 13 Api 33 - runtime notification permission has been added
    val notificationPermissionState = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS
    )
    if (showNotificationDialog.value) FirebaseMessagingNotificationPermissionDialog(
        showNotificationDialog = showNotificationDialog,
        notificationPermissionState = notificationPermissionState
    )

    LaunchedEffect(key1=Unit){
        if (notificationPermissionState.status.isGranted ||
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
        ) {
            Firebase.messaging.subscribeToTopic("s")
        } else showNotificationDialog.value = true
    }



    when(contactListUiState){
        is ContactListUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
        is ContactListUiState.Success -> {
            val usersList = (contactListUiState as  ContactListUiState.Success).usersList
            ContactsList(usersList, navController )
        }
        is ContactListUiState.Error -> ErrorScreen()
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ContactsList(userList: List<User>, navController: NavController){
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(userList.size) { user ->
            Contact(
                userList[user],
                navController
            )
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Contact(user:User, navController: NavController){
    val currentUser = FirebaseAuth.getInstance().currentUser
    var unreadMessageCount by rememberSaveable { mutableIntStateOf(0) }
    val messageViewModel :MessageViewModel = viewModel()
    LaunchedEffect(user.id) {
        messageViewModel.getUnreadMessageCountForContact(user.id, current_userId) { count ->
             unreadMessageCount = count
            if (unreadMessageCount > 0) {
                messageViewModel.sendNotification = true
            }
        }
    }

    Card(
        onClick = {
            if(currentUser!=null){
            navController.navigate("Message/${user.name}/${user.id}")
            }
            else{
                navController.navigate(Screens.Profile.name)
            }
                  },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row {
            Image(
                painter = painterResource(R.drawable.no_image),
                contentDescription ="",
                modifier = Modifier.size(60.dp)
            )
            Column {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(end = 8.dp, top=8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = user.name,
                        modifier = Modifier.padding(start = 4.dp),
                        fontWeight = FontWeight(700),
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier
                        .width(12.dp)
                        .weight(1f)  )
                    if(unreadMessageCount>0) {
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.onPrimary, CircleShape)
                                .size(30.dp)
                                .align(Alignment.CenterVertically)
                        ) {
                            Text(
                                text = unreadMessageCount.toString(),
                                modifier = Modifier.align(Alignment.Center),
                                fontSize = 14.sp,
                                fontWeight = FontWeight(400)
                            )
                        }
                    }

                }

                Text(
                    text = user.email,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp),
                )
            }
        }
    }
}

@Composable
fun fakeNavController(): NavHostController {
    return rememberNavController() // This can be a simple rememberNavController for preview purposes
}
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
@Preview
fun PreviewContact(){
    Contact(user = User("","Ahmad Yousafzai","ahmadabidyousafzai@gmail.com"), navController = fakeNavController() )
}

