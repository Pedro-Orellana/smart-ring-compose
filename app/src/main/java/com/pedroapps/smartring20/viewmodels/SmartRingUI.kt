package com.pedroapps.smartring20.viewmodels

data class SmartRingUI(
    val address: String,
    val ringName: String
)  {

    companion object {
        fun emptySmartRingUI() : SmartRingUI {

            return SmartRingUI(
                address = "",
                ringName = ""
            )
        }
    }

}
