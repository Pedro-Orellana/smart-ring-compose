package com.pedroapps.smartring20.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pedroapps.smartring20.components.NoRingCardLayout

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    startScanning: () -> Unit
) {

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)) {

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
        ) {

            Text(
                text = "Home",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(start = 12.dp, top = 12.dp)
            )

            HorizontalDivider(
                modifier = Modifier
                    .padding(12.dp)
            )

            Spacer(
                modifier = Modifier
                    .padding(20.dp)
            )

            Text(
                text = "Welcome to the Smart Ring app!",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp)
                )

        }

        NoRingCardLayout(
            startScanning = startScanning
        )

    }


}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {

    val paddingValues = PaddingValues()

    HomeScreen(
        paddingValues = paddingValues,
        startScanning = {}
    )
}