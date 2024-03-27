package com.pedroapps.smartring20.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mbientlab.metawear.module.Led

@Composable
fun DevicesScreen(
    paddingValues: PaddingValues,
    currentLedColor: Led.Color?,
    editLedPattern: (Led.Color) -> Unit,
    turnLedOff: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    )
    {
        Text(
            text = "Devices screen",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        Text(
            text = "This is a little test to make sure the service works correctly",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(top = 12.dp, bottom = 12.dp)
            )

        TextButton(onClick = {
            if (currentLedColor == Led.Color.GREEN){
                turnLedOff()
            } else editLedPattern(Led.Color.GREEN)
        }) {
            Text(
                text = if(currentLedColor == Led.Color.GREEN) "Green OFF" else "Green ON",
                color = Color.Green,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp)
        }

        TextButton(onClick = {
            if (currentLedColor == Led.Color.RED){
                turnLedOff()
            } else editLedPattern(Led.Color.RED)
        }) {
            Text(
                text = if(currentLedColor == Led.Color.RED) "Red OFF" else "Red ON",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp)
        }

        TextButton(onClick = {
            if (currentLedColor == Led.Color.BLUE){
                turnLedOff()
            } else editLedPattern(Led.Color.BLUE)

        }) {
            Text(
                text = if(currentLedColor == Led.Color.BLUE) "Blue OFF" else "Blue ON",
                color = Color.Blue,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DevicesScreenPreview() {
    DevicesScreen(
        paddingValues = PaddingValues(),
        currentLedColor = null,
        editLedPattern = {},
        turnLedOff = {}
        )
}