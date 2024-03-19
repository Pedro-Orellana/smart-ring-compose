package com.pedroapps.smartring20.viewmodels

data class RingAppState(
    var registeredRing: String = "", //needs to be of type SmartRing
    var registeredDevices: List<String> = listOf() //needs to be of type List<SmartDevice>
)
