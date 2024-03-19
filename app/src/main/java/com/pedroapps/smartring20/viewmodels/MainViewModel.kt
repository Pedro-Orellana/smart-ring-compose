package com.pedroapps.smartring20.viewmodels

import android.Manifest
import android.app.Application
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import com.pedroapps.smartring20.ble.RingScanCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(
    private val application: Application
): AndroidViewModel(application) {

    private val _appState = MutableStateFlow(RingAppState())
    val appState = _appState.asStateFlow()

    private val bluetoothManager = application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter = bluetoothManager.adapter

    fun startScanning() {
        if(bluetoothAdapter.isEnabled) {

            println("This should start scanning...")

            val bleScanner = bluetoothAdapter.bluetoothLeScanner

            val scanFilter = ScanFilter.Builder()
                .setDeviceAddress("E9:F1:75:BC:C3:08")
                .build()

            val scanFilters: List<ScanFilter> = listOf(scanFilter)

            val settings = ScanSettings.Builder().build()

            val scanCallback = RingScanCallback()

            if (ActivityCompat.checkSelfPermission(
                    application,
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                println("The function is getting returned without scanning")
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            bleScanner.startScan(scanFilters, settings, scanCallback)


        }
    }

}