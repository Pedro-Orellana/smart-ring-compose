package com.pedroapps.smartring20.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mbientlab.metawear.data.EulerAngles
import com.mbientlab.metawear.module.Led
import com.pedroapps.smartring20.components.GestureTestingTab
import com.pedroapps.smartring20.components.LedTestingTab
import com.pedroapps.smartring20.components.TapTestingTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestingScreen(
    paddingValues: PaddingValues,
    currentLedColor: Led.Color?,
    turnLedOff: () -> Unit,
    editLedPattern: (Led.Color) -> Unit,
    startTapTesting: () -> Unit,
    stopTapTesting: () -> Unit,
    currentTapCount: Int,
    startGestureTesting: () -> Unit,
    stopGestureTesting: () -> Unit,
    rawAngleData: EulerAngles?
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        val tabs = listOf("LED", "Tap", "Gesture")
        val selectedTab = remember {
            mutableIntStateOf(0)
        }
        PrimaryTabRow(
            selectedTabIndex = selectedTab.intValue,
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = index == selectedTab.intValue,
                    onClick = { selectedTab.intValue = index },
                ) {
                    Text(
                        text = title,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    )
                }
            }
        }

        when (selectedTab.intValue) {
            0 -> LedTestingTab(
                currentLedColor = currentLedColor,
                turnLedOff = turnLedOff,
                editLedPattern = editLedPattern
            )
            1 -> TapTestingTab(
                startTapTesting = startTapTesting,
                stopTapTesting = stopTapTesting,
                currentTapCount = currentTapCount
            )
            2 -> GestureTestingTab(
                startGestureTesting = startGestureTesting,
                stopGestureTesting = stopGestureTesting,
                rawAngleData = rawAngleData
            )
        }


    }
}


@Preview(showBackground = true)
@Composable
fun TestingScreenPreview() {
    TestingScreen(
        paddingValues = PaddingValues(),
        currentLedColor = null,
        turnLedOff = { },
        editLedPattern = { },
        startTapTesting = {},
        stopTapTesting = {},
        currentTapCount = 0,
        startGestureTesting = {},
        stopGestureTesting = {},
        rawAngleData = null
    )
}