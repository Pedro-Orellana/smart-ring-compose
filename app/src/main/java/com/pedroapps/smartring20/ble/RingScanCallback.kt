package com.pedroapps.smartring20.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult

class RingScanCallback(
    private val stopScanning: () -> Unit,
    private val updateUI: (BluetoothDevice) -> Unit
): ScanCallback() {
    @SuppressLint("MissingPermission")
    override fun onScanResult(callbackType: Int, result: ScanResult?) {
        super.onScanResult(callbackType, result)
        val bluetoothDevice = result?.device
        bluetoothDevice?.let {
           updateUI(it)
            stopScanning()
        }
    }
}