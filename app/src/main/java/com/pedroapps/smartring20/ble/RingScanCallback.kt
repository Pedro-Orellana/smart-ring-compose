package com.pedroapps.smartring20.ble

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult

class RingScanCallback: ScanCallback() {
    override fun onScanResult(callbackType: Int, result: ScanResult?) {
        super.onScanResult(callbackType, result)
        //TODO(do something here with the result of the scan
        println("A result was found!")
    // like sending the result back to viewmodel and update state with it)
        val bluetoothDevice = result?.device
        bluetoothDevice?.let {
            println("It's address is: ${it.address}")
        }
    }
}