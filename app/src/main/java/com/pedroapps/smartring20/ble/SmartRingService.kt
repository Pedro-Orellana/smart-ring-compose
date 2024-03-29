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
import android.os.Binder
import android.os.IBinder
import com.mbientlab.metawear.MetaWearBoard
import com.mbientlab.metawear.android.BtleService
import com.mbientlab.metawear.data.TapType
import com.mbientlab.metawear.module.AccelerometerBosch
import com.mbientlab.metawear.module.AccelerometerBosch.Tap
import com.mbientlab.metawear.module.Led
import com.mbientlab.metawear.module.Led.Color
import com.pedroapps.smartring20.BLUETOOTH_DEVICE_ADDRESS
import com.pedroapps.smartring20.FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID
import com.pedroapps.smartring20.FOREGROUND_SERVICE_NOTIFICATION_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SmartRingService : Service(), ServiceConnection {

    private var bluetoothDevice: BluetoothDevice? = null
    private var btleService: BtleService.LocalBinder? = null
    private var smartRing: MetaWearBoard? = null

    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var notificationManager: NotificationManager

    private val _ringState = MutableStateFlow(SmartRingState())
    val ringState = _ringState.asStateFlow()

    //RING MODULES
    private var ledModule: Led? = getLedModule()
    private var accBosch: AccelerometerBosch? = getAccBoschModule()

    private val job = SupervisorJob()
    private val mainScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate() {
        super.onCreate()
        bluetoothManager = getSystemService(BluetoothManager::class.java) as BluetoothManager
        notificationManager =
            getSystemService(NotificationManager::class.java) as NotificationManager
    }


    //CONNECTING TO BTLESERVICE
    override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
        println("btleService is bound")
        binder?.let {
            btleService = it as BtleService.LocalBinder
            smartRing = btleService?.getMetaWearBoard(bluetoothDevice)
            connectToRing()
        }
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        println("btleService is disconnected")
        btleService = null
    }


    override fun onBind(intent: Intent?): IBinder? {

        val deviceAddress = intent?.getStringExtra(BLUETOOTH_DEVICE_ADDRESS) ?: ""
        println("device address: $deviceAddress")

        if (deviceAddress.isNotEmpty()) {
            bluetoothDevice = bluetoothManager.adapter.getRemoteDevice(deviceAddress)
            bindBtleService()
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
        startForeground(
            FOREGROUND_SERVICE_NOTIFICATION_ID,
            createNotification(context = applicationContext),
        )
        return super.onStartCommand(intent, flags, startId)
    }


    //CUSTOM METHODS
    private fun createNotification(context: Context): Notification {

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
        bindService(btleIntent, this, Context.BIND_AUTO_CREATE)
    }


    //METAWEAR BOARD METHODS

    private fun connectToRing() {
        smartRing?.connectAsync()
            ?.continueWith { task ->
                val isConnected = !task.isFaulted
                updateRingConnectionState(isConnected = isConnected)
                initializeRingModules()
                configureDoubleTapDetection()
            }
    }

    fun getRingInformation() {
        smartRing?.let {
            println("board address: ${it.macAddress}")
            println("board connected: ${it.isConnected}")
            println("board model: ${it.model.name}")
        }
    }

    private fun initializeRingModules() {
        //TODO(add here all the necessary modules)
        ledModule = getLedModule()
        accBosch = getAccBoschModule()
    }

    private fun getLedModule() : Led? {
        return ledModule ?: smartRing?.getModule(Led::class.java)
    }

    private fun getAccBoschModule(): AccelerometerBosch? {
        return accBosch ?: smartRing?.getModule(AccelerometerBosch::class.java)
    }


     fun editLedPattern(color : Color) {

        ledModule?.let { led ->
            led.stop(true)
            led.editPattern(
                color,
                Led.PatternPreset.SOLID
            ).commit()

            led.play()

            updateLedColorState(color)

        }

    }

   private fun configureDoubleTapDetection() {
        //TODO(separate the start and the configure into 2 methods)
        accBosch?.let { acc ->
            acc.tap().configure()
                .enableDoubleTap()
                .threshold(2f)
                .shockTime(AccelerometerBosch.TapShockTime.TST_75_MS)

                .commit()

            acc.tap().addRouteAsync { source ->
                source.stream { data, _ ->
                    val tapData = data.value(Tap::class.java)
                    if (tapData.type == TapType.DOUBLE) {
                        mainScope.launch(Dispatchers.Main) {
                            println("Double tap!")
                            addToDoubleTapCount()
                        }
                    }
                }
            }
        }
    }

    fun startDoubleTapDetection() {
        accBosch?.let { acc ->
            acc.tap().start()
            acc.start()
        }
    }

    fun stopDoubleTapDetection() {
        accBosch?.let { acc ->
            acc.tap().stop()
            acc.stop()
            resetDoubleTapCount()
        }
    }

     fun turnLedOff() {
         updateLedColorState(null)
        ledModule?.stop(true)
    }


    //RING STATE METHODS

    private fun updateRingConnectionState(isConnected: Boolean) {
        _ringState.update { currentState ->
            currentState.copy(isConnected = isConnected)
        }
    }

    private fun updateLedColorState(color: Color?) {
        _ringState.update { currentState ->
            currentState.copy(currentLedColor = color)
        }
    }

    private fun addToDoubleTapCount() {
        _ringState.update { currentState ->
            val newTapCount = currentState.doubleTapCount + 1
            println("new count: $newTapCount")
            currentState.copy(doubleTapCount = newTapCount)
        }
    }

    private fun resetDoubleTapCount() {
        _ringState.update { currentState ->
            currentState.copy(doubleTapCount = 0)
        }
    }


    inner class SmartRingBinder : Binder() {
        fun getService(): SmartRingService = this@SmartRingService
    }


}