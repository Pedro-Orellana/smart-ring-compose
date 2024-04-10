package com.pedroapps.smartring20.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mbientlab.metawear.data.EulerAngles
import com.mbientlab.metawear.module.Led


@Composable
fun LedTestingTab(
    currentLedColor: Led.Color?,
    turnLedOff: () -> Unit,
    editLedPattern: (Led.Color) -> Unit
) {

    DisposableEffect(key1 = true) {
        println("Led entering composition")
        onDispose(turnLedOff)
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {

        Text(
            text = "Press any of the buttons to interact with the LED",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth(0.60f)
                .padding(bottom = 12.dp)
        )

        TextButton(onClick = {
            if (currentLedColor == Led.Color.GREEN) {
                turnLedOff()
            } else editLedPattern(Led.Color.GREEN)
        }) {
            Text(
                text = if (currentLedColor == Led.Color.GREEN) "Green OFF" else "Green ON",
                color = Color.Green,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        TextButton(onClick = {
            if (currentLedColor == Led.Color.RED) {
                turnLedOff()
            } else editLedPattern(Led.Color.RED)
        }) {
            Text(
                text = if (currentLedColor == Led.Color.RED) "Red OFF" else "Red ON",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        TextButton(onClick = {
            if (currentLedColor == Led.Color.BLUE) {
                turnLedOff()
            } else editLedPattern(Led.Color.BLUE)

        }) {
            Text(
                text = if (currentLedColor == Led.Color.BLUE) "Blue OFF" else "Blue ON",
                color = Color.Blue,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }

}

@Composable
fun TapTestingTab(
    startTapTesting: () -> Unit,
    stopTapTesting: () -> Unit,
    currentTapCount: Int
) {

    DisposableEffect(key1 = true) {
        startTapTesting()
        onDispose {
            println("stopping tap testing")
            stopTapTesting()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Count of double taps:",
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(bottom = 20.dp)
        )
        Text(
            text = currentTapCount.toString(),
            fontSize = 18.sp,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
fun GestureTestingTab(
    startGestureTesting: () -> Unit,
    stopGestureTesting: () -> Unit,
    rawAngleData: EulerAngles?
) {

    DisposableEffect(key1 = true) {
        startGestureTesting()
        onDispose {
            stopGestureTesting()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Gesture Testing",
            textAlign = TextAlign.Center,
            fontSize = 24.sp
            )

        Text(
            text = "Current angle measurements:",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(8.dp)
        )
        Row(
            modifier = Modifier
                .padding(4.dp)
        ) {
            Text(text = "Roll: ")
            Text(text = rawAngleData?.roll().toString())
        }

        Row(
            modifier = Modifier
                .padding(4.dp)
        ) {
            Text(text = "Yaw: ")
            Text(text = rawAngleData?.yaw().toString())
        }

        Row(
            modifier = Modifier
                .padding(4.dp)
        ) {
            Text(text = "Pitch: ")
            Text(text = rawAngleData?.pitch().toString())
        }

        Row(
            modifier = Modifier
                .padding(4.dp)
        ) {
            Text(text = "Heading: ")
            Text(text = rawAngleData?.heading().toString())
        }
    }

}


@Preview(showBackground = true)
@Composable
fun TapTestingTabPreview() {
    TapTestingTab(
        startTapTesting = {},
        stopTapTesting = {},
        currentTapCount = 0
    )
}

@Preview(showBackground = true)
@Composable
fun GestureTestingTabPreview() {
    GestureTestingTab(
        startGestureTesting = {},
        stopGestureTesting = {},
        rawAngleData = null
    )
}