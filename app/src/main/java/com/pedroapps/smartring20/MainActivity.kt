package com.pedroapps.smartring20

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pedroapps.smartring20.components.AppBottomBar
import com.pedroapps.smartring20.screens.Destinations
import com.pedroapps.smartring20.screens.DevicesScreen
import com.pedroapps.smartring20.screens.GesturesScreen
import com.pedroapps.smartring20.screens.HomeScreen
import com.pedroapps.smartring20.screens.NewDeviceScreen
import com.pedroapps.smartring20.screens.SettingsScreen
import com.pedroapps.smartring20.ui.theme.SmartRing20Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           AppContent()
        }
    }
}


@Composable
fun AppContent() {
    SmartRing20Theme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ContainerContent()
        }
    }
}


@Composable
fun ContainerContent() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { AppBottomBar(navController = navController) },
        content = { paddingValues ->

            NavHost(navController = navController , startDestination = Destinations.HomeScreen ) {

                composable(route = Destinations.DevicesScreen) {
                    DevicesScreen(paddingValues = paddingValues)
                }

                composable(route = Destinations.GesturesScreen) {
                    GesturesScreen(paddingValues = paddingValues)
                }
                composable(route = Destinations.HomeScreen) {
                    HomeScreen(paddingValues = paddingValues)
                }
                composable(route = Destinations.NewDeviceScreen) {
                    NewDeviceScreen(paddingValues = paddingValues)
                }
                composable(route = Destinations.SettingsScreen) {
                    SettingsScreen(paddingValues = paddingValues)
                }


            }

        }
    )
}



@Preview(showBackground = true)
@Composable
fun ContainerContentPreview() {
  AppContent()
}
