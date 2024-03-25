package com.pedroapps.smartring20

import android.Manifest
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pedroapps.smartring20.ble.SmartRingService
import com.pedroapps.smartring20.components.AppBottomBar
import com.pedroapps.smartring20.screens.Destinations
import com.pedroapps.smartring20.screens.DevicesScreen
import com.pedroapps.smartring20.screens.GesturesScreen
import com.pedroapps.smartring20.screens.HomeScreen
import com.pedroapps.smartring20.screens.NewDeviceScreen
import com.pedroapps.smartring20.screens.SettingsScreen
import com.pedroapps.smartring20.ui.theme.SmartRing20Theme
import com.pedroapps.smartring20.viewmodels.MainViewModel

class MainActivity : ComponentActivity(), ServiceConnection {

    private lateinit var mainViewModel: MainViewModel
    private var smartRingIntent: Intent? = null
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        setContent {
            mainViewModel = viewModel()

            LaunchedEffect(key1 = mainViewModel) {
                if(this@MainActivity::mainViewModel.isInitialized) {
                    rebindServiceIfNeeded()
                }
            }

            val fineLocationPermissionLauncher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    val message = if (isGranted) "All necessary permissions granted"
                    else "please grant all permissions for app to work properly"

                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                }


            val bluetoothPermissionsLauncher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    val scanPermission = permissions[Manifest.permission.BLUETOOTH_SCAN]
                    val connectPermission = permissions[Manifest.permission.BLUETOOTH_CONNECT]

                    if (scanPermission == true && connectPermission == true) {
                        //it doesn't say in the official documentation, but to be able to
                        //scan bluetooth devices, fine location is required
                        fineLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }



            LaunchedEffect(
                key1 = bluetoothPermissionsLauncher,
            ) {
                bluetoothPermissionsLauncher.launch(
                    arrayOf(
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT
                    )
                )
            }
            AppContent(
                viewModel = mainViewModel,
                bindToSmartRingService = this::bindToSmartRingService
            )
        }
    }

    override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
        println("SmartRingService is bound")
        binder?.let{
            val smartRingService = (it as SmartRingService.SmartRingBinder).getService()
            smartRingService.getRingInformation()
            mainViewModel.updateSmartRingService(smartRingService)
            startForegroundService(smartRingIntent)
        }
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        println("SmartRingService is unbound")
        mainViewModel.updateSmartRingService(null)
    }

    private fun bindToSmartRingService(ringAddress: String) {
        val intent = getSmartRingServiceIntent(ringAddress)
        bindService(intent,this, BIND_AUTO_CREATE)
    }

    private fun getSmartRingServiceIntent(ringAddress: String) : Intent {
        return smartRingIntent ?: run {
            val intent = Intent(this, SmartRingService::class.java)
            intent.putExtra(BLUETOOTH_DEVICE_ADDRESS, ringAddress)
            smartRingIntent = intent
            intent
        }

    }

    private fun rebindServiceIfNeeded() {
        val activityManager = getSystemService(ActivityManager::class.java) as ActivityManager
        val services = activityManager.getRunningServices(Integer.MAX_VALUE)
        for(service in services) {
            if(service.service.className == SMART_RING_SERVICE_CLASS_NAME) {
                //rebind the service here
                //TODO(this crashes my app because viewModel has not been initialized yet,
                // so don't get the info from viewModel, get it from sharedPreferences or dataStore)
                val intent = Intent(this, SmartRingService::class.java)
                smartRingIntent = intent
                bindService(intent, this, BIND_AUTO_CREATE)
            }
        }
    }

}


@Composable
fun AppContent(
    viewModel: MainViewModel = viewModel(),
    bindToSmartRingService: (String) -> Unit
) {
    SmartRing20Theme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ContainerContent(
                viewModel = viewModel,
                bindToSmartRingService = bindToSmartRingService
            )
        }
    }
}


@Composable
fun ContainerContent(
    viewModel: MainViewModel = viewModel(),
    bindToSmartRingService: (String) -> Unit
) {

    val navController = rememberNavController()
    val appState = viewModel.appState.collectAsState()

    Scaffold(
        bottomBar = { AppBottomBar(navController = navController) },
        content = { paddingValues ->

            NavHost(navController = navController, startDestination = Destinations.HomeScreen) {

                composable(route = Destinations.DevicesScreen) {
                    DevicesScreen(paddingValues = paddingValues)
                }

                composable(route = Destinations.GesturesScreen) {
                    GesturesScreen(paddingValues = paddingValues)
                }
                composable(route = Destinations.HomeScreen) {
                    HomeScreen(
                        paddingValues = paddingValues,
                        isScanning = appState.value.isScanning,
                        startScanning = viewModel::startRingScanning,
                        stopScanning = viewModel::stopRingScanning,
                        foundSmartRing = appState.value.foundSmartRing,
                        dismissFoundSmartRing = { viewModel.updateFoundSmartRing(null) },

                        saveAndConnectRing = { device ->
                            viewModel.saveNewRing(device)
                            viewModel.connectToRing(device)
                        },
                        registeredRing = appState.value.registeredRing,
                        deleteRing = viewModel::deleteRing,
                        bindToSmartRingService = bindToSmartRingService
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
    AppContent(
        bindToSmartRingService = {}
    )
}
