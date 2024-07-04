package com.example.messaging

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun Contacts(){
    Column(modifier = Modifier.fillMaxSize()){
        contact(name = "Ahmad")
    }
}

@Composable
fun contact(name: String){
    Card(onClick = { /*TODO*/ }, modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp)) {
       // Image(painter = painterResource(id = ), contentDescription = )
       getAllUserNames { names ->
           name.forEach { _ ->
               println(name)
           }
       }
    }
}

fun storeUserInfo() {
    val user = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()

    user?.let {
        val userInfo = hashMapOf(
            "name" to it.displayName,
            "email" to it.email
        )

        db.collection("users").document(it.uid)
            .set(userInfo)
            .addOnSuccessListener {
                // User information stored successfully
            }
            .addOnFailureListener { e ->
                // Handle failure
            }
    }
}

