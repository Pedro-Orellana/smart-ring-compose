package com.pedroapps.smartring20.components

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
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

//    val isScanning = remember {
//        mutableStateOf(false)
//    }

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
fun RingCard(
    registeredRing: SmartRingUI
) {
    val isRotated = remember {
        mutableStateOf(false)
    }

    val rotation by animateFloatAsState(
        targetValue = if (isRotated.value) 180f else 0f,
        animationSpec = tween(500),
        label = "card rotation state"
    )

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
                registeredRing = registeredRing
            )
        } else {
            RingCardFront(
                isRotated = isRotated,
                registeredRing = registeredRing
            )
        }
    }
}


@Composable
private fun RingCardFront(
    isRotated: MutableState<Boolean>,
    registeredRing: SmartRingUI
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
                Text(text = "Disconnected")
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
    registeredRing: SmartRingUI
) {
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
            Text(text = "Back of card")
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

    }

}


@SuppressLint("MissingPermission")
@Composable
fun SmartRingFoundDialog(
    foundRing: BluetoothDevice?,
    dismissFoundRing: () -> Unit
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
            TextButton(onClick = { /*TODO*/ }) {
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


@Preview(showBackground = true)
@Composable
fun RingCardPreview() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        val ring = SmartRingUI(
            address = "AB:CD:EF:GH:IJ",
            ringName = "MetaWear"
        )

        RingCard(
            registeredRing = ring
        )
    }

}


@Preview(showBackground = true)
@Composable
fun SmartRingFoundDialogPreview() {

    SmartRingFoundDialog(
        foundRing = null,
        dismissFoundRing = {}
    )
}
