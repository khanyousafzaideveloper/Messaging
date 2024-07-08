package com.example.messaging

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messaging.signIn.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch


sealed interface ContactListUiState{
    data class Success(val usersList:List<User>): ContactListUiState
    object Error: ContactListUiState
    object Loading: ContactListUiState
}

data class User(

    val id: String ="",
    val name: String = "",
    val email: String = ""
){
    constructor():this("", "","")
}

class ContactListViewModel: ViewModel(){

    private val _contactListUiState = mutableStateOf<ContactListUiState>(ContactListUiState.Loading)
    val contactListUiState : State<ContactListUiState> = _contactListUiState
    val currentUser = FirebaseAuth.getInstance().currentUser
    val db = Firebase.firestore


    fun storeUserInfo() {
        currentUser?.let {
            val userInfo = hashMapOf(
                "id" to it.uid,
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


    init {
        getDataOfUsers()
    }



    private fun getDataOfUsers() {
        viewModelScope.launch {
            try {
                db.collection("users").addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w(ContentValues.TAG, "Error getting documents", e)
                        _contactListUiState.value = ContactListUiState.Error
                        return@addSnapshotListener
                    }

                    if ((snapshot != null) && !snapshot.isEmpty) {
                        val contactListObjects = snapshot.toObjects(User::class.java)
                        val filteredList = contactListObjects.filter { user ->
                            // Filter out the current user's data based on their unique identifier
                            user.name != currentUser?.displayName
                        }
                        _contactListUiState.value = ContactListUiState.Success(filteredList)
                    } else {
                        _contactListUiState.value = ContactListUiState.Success(emptyList())
                    }
                }
            } catch (exception: Exception) {
                Log.w(ContentValues.TAG, "Error getting documents", exception)
                _contactListUiState.value = ContactListUiState.Error
            }
        }
    }


}