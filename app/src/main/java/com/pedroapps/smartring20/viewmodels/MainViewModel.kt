package com.pedroapps.smartring20.viewmodels

import android.Manifest
import android.app.Application
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pedroapps.smartring20.ble.RingScanCallback
import com.pedroapps.smartring20.database.SmartRingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val _appState = MutableStateFlow(RingAppState())
    val appState = _appState.asStateFlow()

    private val ringRepository = SmartRingRepository(application)

    private val bluetoothManager =
        application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter = bluetoothManager.adapter

    private val ringScanCallback = RingScanCallback(
        updateUI = this::updateFoundSmartRing,
        stopScanning = this::stopRingScanning
    )


    init {
        viewModelScope.launch(Dispatchers.IO) {
            ringRepository.getRegisteredRing().distinctUntilChanged()
                .collect { smartRing ->
                    val updatedRing = smartRing?.toSmartRingUI() ?: SmartRingUI.emptySmartRingUI()
                    updateRingUIState(updatedRing)
                }
        }
    }


    //STATE FUNCTIONS


    private fun updateIsScanning(isScanning: Boolean) {
        _appState.update { currentState ->
            currentState.copy(isScanning = isScanning)
        }
    }

    private fun updateRingUIState(smartRing: SmartRingUI) {
        _appState.update { currentState ->
            currentState.copy(registeredRing = smartRing)
        }
    }

    fun updateFoundSmartRing(bluetoothDevice: BluetoothDevice?) {
        _appState.update { currentState ->
            currentState.copy(foundSmartRing = bluetoothDevice)
        }
    }


    //DATABASE FUNCTIONS

    fun saveNewRing(device: BluetoothDevice) {
        viewModelScope.launch {
            ringRepository.insertNewRing(device)
        }
    }

    fun deleteRing(smartRing: SmartRingUI) {
        viewModelScope.launch {
            ringRepository.deleteRegisteredRing(smartRing)
        }
    }


    //BLUETOOTH FUNCTIONS


    //SCANNING

    fun startRingScanning() {
        if (bluetoothAdapter.isEnabled) {

            updateIsScanning(true)

            val bleScanner = bluetoothAdapter.bluetoothLeScanner

            val scanFilter = ScanFilter.Builder()
                .setDeviceAddress("E9:F1:75:BC:C3:08")
                .build()

            val scanFilters: List<ScanFilter> = listOf(scanFilter)

            val settings = ScanSettings.Builder().build()

            if (ActivityCompat.checkSelfPermission(
                    application,
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            bleScanner.startScan(scanFilters, settings, ringScanCallback)


        }
    }


    fun stopRingScanning() {
        updateIsScanning(false)
        if (ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        bluetoothAdapter.bluetoothLeScanner.stopScan(ringScanCallback)
    }


    //CONNECTING

    fun connectToRing(device: BluetoothDevice) {
        //TODO(finish this method)
        println("Connected to ring!")
    }


}