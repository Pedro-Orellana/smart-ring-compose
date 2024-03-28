package com.pedroapps.smartring20.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun TapTestingTab() {

    DisposableEffect(key1 = true) {
        onDispose {  }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = "Tap Testing tab")
    }
}

@Composable
fun GestureTestingTab() {
    DisposableEffect(key1 = true) {
        onDispose {  }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = "Gesture Testing tab")
    }

}