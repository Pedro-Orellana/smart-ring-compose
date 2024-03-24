package com.pedroapps.smartring20.ble

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.IBinder
import com.mbientlab.metawear.MetaWearBoard
import com.mbientlab.metawear.android.BtleService
import com.pedroapps.smartring20.BLUETOOTH_DEVICE_ADDRESS
import com.pedroapps.smartring20.FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID
import com.pedroapps.smartring20.FOREGROUND_SERVICE_NOTIFICATION_ID

class SmartRingService: Service(), ServiceConnection {

    private var bluetoothDevice: BluetoothDevice? = null
    private var btleService: BtleService.LocalBinder? = null
    private var smartRing: MetaWearBoard? = null

    private val bluetoothManager = getSystemService(BluetoothManager::class.java) as BluetoothManager
    private val notificationManager = getSystemService(NotificationManager::class.java) as NotificationManager


    //CONNECTING TO BTLESERVICE
    override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
        println("btleService is bound")
        binder?.let {
            btleService = it as BtleService.LocalBinder
            smartRing = btleService?.getMetaWearBoard(bluetoothDevice)
        }
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        btleService = null
    }





    override fun onBind(intent: Intent?): IBinder? {
        println("SmartRingService is bound")

        val deviceAddress = intent?.getStringExtra(BLUETOOTH_DEVICE_ADDRESS) ?: ""

        if(deviceAddress.isNotEmpty()) {
            bluetoothDevice = bluetoothManager.adapter.getRemoteDevice(deviceAddress)
            bindBtleService()
            startForeground(
                FOREGROUND_SERVICE_NOTIFICATION_ID,
                createNotification(context = applicationContext),
            )

           return SmartRingBinder()
        }

        return null

    }

    override fun onRebind(intent: Intent?) {
        println("SmartRingService has been rebound")
        super.onRebind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("SmartRingService is foreground started")
        return super.onStartCommand(intent, flags, startId)
    }


    //CUSTOM METHODS
    private fun createNotification(context: Context) : Notification {

        createNotificationChannel()
        return Notification.Builder(context, FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Smart Ring 2.0")
            .setContentText("Ring is successfully connected!")
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID,
            "Foreground service notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }


    private fun bindBtleService() {
        val btleIntent = Intent(this, BtleService::class.java)
        bindService(btleIntent,this, Context.BIND_AUTO_CREATE)
    }


    private inner class SmartRingBinder: Binder() {
        fun getService() : SmartRingService =  this@SmartRingService
    }


}