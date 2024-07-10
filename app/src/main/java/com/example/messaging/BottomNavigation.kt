package com.example.messaging

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    var selectedItemIndex by remember {
        mutableIntStateOf(0)
    }


    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(

                selected = selectedItemIndex == index,
                onClick = {
                    navController.navigate(item.destination)
                    selectedItemIndex = index
                },
                label = {
                    Text(text = item.title)
                },
                icon = {
                    BadgedBox(badge = {
                        if (item.badgeCount != null) {
                            Badge {
                                Text(text = item.badgeCount.toString())
                            }
                        } else if (item.hasNews) {
                            Badge()
                        }

                    }
                    ) {

                        (if (index == selectedItemIndex) {
                            item.selectedItem
                        } else {
                            item.unSelectedItem
                        }).let {
                            Icon(
                                imageVector = it,
                                contentDescription = item.title
                            )
                        }
                    }
                }
            )
        }
    }
}

data class BottomNavigationItem(
    val title: String,
    val selectedItem : ImageVector,
    val unSelectedItem: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null,
    val destination: String
)



val items = listOf(
    BottomNavigationItem(
        title = "Contacts",
        selectedItem = Icons.AutoMirrored.Filled.List,
        unSelectedItem = Icons.AutoMirrored.Filled.List,
        hasNews = false,
        destination = Screens.Contacts.name
    ),
    BottomNavigationItem(
        title = "Profile",
        selectedItem = Icons.Filled.AccountCircle,
        unSelectedItem = Icons.Outlined.AccountCircle,
        hasNews = false,
        destination = Screens.Profile.name
    ),
)