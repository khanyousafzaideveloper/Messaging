package com.example.messaging

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.civicengagementplatform.ui.Messaging.MessageScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ContactScreen(navController: NavController){
    val viewModel: ContactListViewModel = viewModel()
    val contactListUiState by viewModel.contactListUiState

    when(contactListUiState){
        is ContactListUiState.Loading -> LoadingScreen()
        is ContactListUiState.Success -> {
            val usersList = (contactListUiState as  ContactListUiState.Success).usersList
            ContactsList(usersList, navController )
        }
        is ContactListUiState.Error -> ErrorScreen()
    }
}

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

@Composable
fun Contact(user:User, navController: NavController){
    Card(
        onClick = {navController.navigate("Message/${user.name}/${user.id}")},
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row {
            Image(painter = painterResource(R.drawable.no_image), contentDescription ="" )
            Column {
                Text(
                    text = user.name,
                    modifier = Modifier.padding(4.dp),
                    fontWeight = FontWeight(700)
                )
                Text(
                    text = user.email,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

//@SuppressLint("RememberReturnType")
//@Composable
//@Preview
//fun PreviewContact(){
//    Contact(user = User("","Ahmad Yousafzai","ahmadabidyousafzai@gmail.com"), navController ={} )
//}

