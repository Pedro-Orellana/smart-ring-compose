package com.pedroapps.smartring20.components

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
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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


@Composable
fun NoRingCardLayout(
    startScanning: () -> Unit
) {

    val isScanning = remember {
        mutableStateOf(false)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (isScanning.value) {
            ScanningLayout(isScanning = isScanning)
        } else NotScanningLayout(isScanning = isScanning, startScanning = startScanning)
    }
}


@Composable
private fun NotScanningLayout(
    isScanning: MutableState<Boolean>,
    startScanning: () -> Unit
) {
    Text(
        text = "Looks like you still don't have any registered ring, please start scanning to find your ring!",
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(start = 12.dp, end = 12.dp, bottom = 20.dp)
    )
    TextButton(
        onClick = {
            isScanning.value = !isScanning.value
            startScanning()
        }
    ) {
        Text(text = "start scanning")
    }
}

@Composable
private fun ScanningLayout(
    isScanning: MutableState<Boolean>
) {
    CircularProgressIndicator()
    Text(text = "Scanning...")
    Text(text = "Make sure your ring is turned on and in range")
    TextButton(onClick = { isScanning.value = !isScanning.value }) {
        Text(text = "stop scanning")
    }
}


@Composable
fun RingCard() {

    val isRotated = remember {
        mutableStateOf(false)
    }

    val rotation by animateFloatAsState(
        targetValue = if (isRotated.value) 180f else 0f,
        animationSpec = tween(500),
        label = "card rotation state"
    )

    Card(
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
            }
    )
    {
        if (isRotated.value) {
            RingCardBack(
                rotation = rotation,
                isRotated = isRotated
            )
        } else {
            RingCardFront(
                isRotated = isRotated
            )
        }
    }
}


@Composable
private fun RingCardFront(
    isRotated: MutableState<Boolean>
) {


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.smart_ring_image
                ),
                contentDescription = "Smart Ring image",
                modifier = Modifier
                    .size(80.dp),
                contentScale = ContentScale.Fit
            )

            Text(text = "Smart Ring 2.0")

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Status: ")
                Text(text = "Disconnected")
            }

        }


        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxSize()
        ) {
            TextButton(
                onClick = { isRotated.value = !isRotated.value },
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(text = "Show actions")
            }
        }

    }


}

@Composable
private fun RingCardBack(
    rotation: Float,
    isRotated: MutableState<Boolean>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                rotationY = rotation
            }

    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(text = "Back of card")
        }


        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxSize()
        ) {
            TextButton(
                onClick = { isRotated.value = !isRotated.value },
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(text = "Back to details")
            }
        }

    }

}


@Preview(showBackground = true)
@Composable
fun RingCardPreview() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    )
    {
        RingCard()
    }

}
