package com.pedroapps.smartring20.components

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pedroapps.smartring20.R
import com.pedroapps.smartring20.viewmodels.SmartRingUI


@Composable
fun NoRingCardLayout(
    isScanning: Boolean,
    startScanning: () -> Unit,
    stopScanning: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isScanning) {
            ScanningLayout(stopScanning = stopScanning)
        } else NotScanningLayout(startScanning = startScanning)
    }
}


@Composable
private fun NotScanningLayout(
    startScanning: () -> Unit
) {
    Text(
        text = "Looks like you still don't have any registered ring, please start scanning to find your ring!",
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 20.dp)
    )
    TextButton(onClick = {
        startScanning()
    }) {
        Text(text = "start scanning")
    }
}

@Composable
private fun ScanningLayout(
    stopScanning: () -> Unit
) {
    CircularProgressIndicator()
    Text(text = "Scanning...")
    Text(text = "Make sure your ring is turned on and in range")
    TextButton(onClick = {
        stopScanning()
    }) {
        Text(text = "stop scanning")
    }
}


@Composable
fun RingLayout(
    registeredRing: SmartRingUI,
    deleteRing: (SmartRingUI) -> Unit,
    bindToSmartRingService: (String) -> Unit,
    isRingConnected: Boolean?
) {
    val isRotated = remember {
        mutableStateOf(false)
    }

    val rotation by animateFloatAsState(
        targetValue = if (isRotated.value) 180f else 0f,
        animationSpec = tween(500),
        label = "card rotation state"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {

        Spacer(modifier = Modifier.padding(20.dp))
        Card(modifier = Modifier
            .height(300.dp)
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
            }) {
            if (isRotated.value) {

                RingCardBack(
                    rotation = rotation,
                    isRotated = isRotated,
                    registeredRing = registeredRing,
                    deleteRing = deleteRing,
                    bindToSmartRingService = bindToSmartRingService
                )
            } else {
                RingCardFront(
                    isRotated = isRotated,
                    registeredRing = registeredRing,
                    isRingConnected = isRingConnected
                )
            }
        }
    }


}


@Composable
private fun RingCardFront(
    isRotated: MutableState<Boolean>,
    registeredRing: SmartRingUI,
    isRingConnected: Boolean?
) {


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.smart_ring_image
                ),
                contentDescription = "Smart Ring image",
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Fit
            )

            Text(text = "Smart Ring 2.0")

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "name: ")
                Text(text = registeredRing.ringName)
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "address: ")
                Text(text = registeredRing.address)
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Status: ")
                Text(text = if(isRingConnected == true) "Connected!" else "Disconnected")
            }

        }


        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxSize()
        ) {
            TextButton(
                onClick = { isRotated.value = !isRotated.value }, modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Show actions")
            }
        }

    }


}

@Composable
private fun RingCardBack(
    rotation: Float, isRotated: MutableState<Boolean>,
    registeredRing: SmartRingUI,
    deleteRing: (SmartRingUI) -> Unit,
    bindToSmartRingService: (String) -> Unit
) {

    val context = LocalContext.current

    val showDeleteRingDialog = remember {
        mutableStateOf(false)
    }

    val notificationPermissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                bindToSmartRingService(registeredRing.address)
            } else {
                val message = "Please grant notification permission to connect to smart ring"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }

    Box(modifier = Modifier
        .fillMaxSize()
        .graphicsLayer {
            rotationY = rotation
        }

    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            TextButton(onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                else
                    bindToSmartRingService(registeredRing.address)
            }) {
                Text(text = "Connect")
            }
            Spacer(modifier = Modifier.padding(12.dp))

            TextButton(onClick = {
                showDeleteRingDialog.value = true
            }) {
                Text(
                    text = "Delete ring",
                    color = Color.Red
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxSize()
        ) {
            TextButton(
                onClick = { isRotated.value = !isRotated.value }, modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Back to details")
            }
        }
        if (showDeleteRingDialog.value) {

            DeleteRingDialog(
                smartRingUI = registeredRing,
                dismissDeleteRingDialog = { showDeleteRingDialog.value = false },
                deleteRing = deleteRing
            )
        }


    }

}


@SuppressLint("MissingPermission")
@Composable
fun SmartRingFoundDialog(
    foundRing: BluetoothDevice?,
    dismissFoundRing: () -> Unit,
    saveAndConnectRing: (BluetoothDevice) -> Unit
) {
    AlertDialog(
        onDismissRequest = dismissFoundRing,
        icon = {
            Icon(imageVector = Icons.Outlined.Star, contentDescription = "dialog icon")
        },
        title = {
            Text(text = "New Smart Ring Found!")
        },

        text = {
            Column {
                Text(text = "Device name: ${foundRing?.name}")
                Text(text = "Device address: ${foundRing?.address}")
            }
        },
        confirmButton = {
            TextButton(onClick = {
                foundRing?.let(saveAndConnectRing)
                dismissFoundRing()
            }) {
                Text(text = "Register Ring")
            }
        },
        dismissButton = {
            TextButton(onClick = dismissFoundRing) {
                Text(text = "Dismiss")
            }
        }


    )

}


@Composable
fun DeleteRingDialog(
    smartRingUI: SmartRingUI,
    dismissDeleteRingDialog: () -> Unit,
    deleteRing: (SmartRingUI) -> Unit
) {
    AlertDialog(
        onDismissRequest = dismissDeleteRingDialog,
        icon = {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "delete icon",
                tint = Color.Red
            )
        },
        title = {
            Text(text = "Are you sure you want to delete this ring?")
        },
        text = {
            Text(text = "If you delete this ring, all the configurations will be lost")
        },
        confirmButton = {
            TextButton(onClick = {
                deleteRing(smartRingUI)
                dismissDeleteRingDialog()
            }) {
                Text(text = "Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = dismissDeleteRingDialog) {
                Text(text = "Cancel")
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun RingCardPreview() {
    val ring = SmartRingUI(
        address = "AB:CD:EF:GH:IJ",
        ringName = "MetaWear"
    )

    RingLayout(
        registeredRing = ring,
        deleteRing = {},
        bindToSmartRingService = {},
        isRingConnected = false
    )
}


@Preview(showBackground = true)
@Composable
fun SmartRingFoundDialogPreview() {

    SmartRingFoundDialog(
        foundRing = null,
        dismissFoundRing = {},
        saveAndConnectRing = {}
    )
}


@Preview(showBackground = true)
@Composable
fun DeleteRingDialogPreview() {
    DeleteRingDialog(
        smartRingUI = SmartRingUI.emptySmartRingUI(),
        dismissDeleteRingDialog = {},
        deleteRing = {}
    )
}
