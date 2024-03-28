package com.pedroapps.smartring20.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.pedroapps.smartring20.screens.Destinations

@Composable
fun AppBottomBar(
    navController: NavHostController
) {
    BottomAppBar(
        actions = {
            IconButton(onClick = { navController.navigate(Destinations.HOME_SCREEN) }) {
                Icon(imageVector = Icons.Outlined.Home, contentDescription = "Home Screen")
            }

            IconButton(onClick = { navController.navigate(Destinations.DEVICES_SCREEN) }) {
                Icon(imageVector = Icons.Outlined.Create, contentDescription = "Devices Screen")
            }

            IconButton(onClick = { navController.navigate(Destinations.GESTURES_SCREEN) }) {
                Icon(imageVector = Icons.Outlined.ThumbUp, contentDescription = "Gestures Screen")
            }

            IconButton(onClick = { navController.navigate(Destinations.SETTINGS_SCREEN) }) {
                Icon(imageVector = Icons.Outlined.Build, contentDescription = "Settings Screen")
            }

            IconButton(onClick = { navController.navigate(Destinations.TESTING_SCREEN) }) {
                Icon(imageVector = Icons.Outlined.CheckCircle, contentDescription = "Testing Screen")
            }
        },

        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Destinations.NEW_DEVICE_SCREEN) }) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add new device")
            }
        }
    )
}