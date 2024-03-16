package com.pedroapps.smartring20.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Build
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
            IconButton(onClick = { navController.navigate(Destinations.HomeScreen) }) {
                Icon(imageVector = Icons.Outlined.Home, contentDescription = "Home Screen")
            }

            IconButton(onClick = { navController.navigate(Destinations.DevicesScreen) }) {
                Icon(imageVector = Icons.Outlined.Create, contentDescription = "Devices Screen")
            }

            IconButton(onClick = { navController.navigate(Destinations.GesturesScreen) }) {
                Icon(imageVector = Icons.Outlined.ThumbUp, contentDescription = "Gestures Screen")
            }

            IconButton(onClick = { navController.navigate(Destinations.SettingsScreen) }) {
                Icon(imageVector = Icons.Outlined.Build, contentDescription = "Settings Screen")
            }
        },

        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Destinations.NewDeviceScreen) }) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add new device")
            }
        }
    )
}