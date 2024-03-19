package com.pedroapps.smartring20

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.pedroapps.smartring20.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val bluetoothPermissionsLauncher =
              rememberLauncherForActivityResult(
                  contract = ActivityResultContracts.RequestPermission()) { isGranted ->
                  val message = if(isGranted) "able to perform bluetooth scans!"
                  else "Please grant this permission for this app to work properly"

                  Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
              }


            val coarseLocationPermissionLauncher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()) { isGranted ->
                    val message = if(isGranted) "Location permission granted!"
                        else "please grant location permission"
                }



            LaunchedEffect(key1 = bluetoothPermissionsLauncher, key2 = coarseLocationPermissionLauncher) {
                bluetoothPermissionsLauncher.launch(Manifest.permission.BLUETOOTH_SCAN)
                //it doesn't say in the official documentation, but to be able to
                //scan bluetooth devices, fine location is required
                coarseLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
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
fun ContainerContent(
    viewModel: MainViewModel = viewModel()
) {

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
                    HomeScreen(
                        paddingValues = paddingValues,
                        startScanning = viewModel::startScanning
                        )
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
