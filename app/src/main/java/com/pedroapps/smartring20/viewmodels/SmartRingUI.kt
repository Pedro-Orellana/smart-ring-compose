package com.pedroapps.smartring20.viewmodels

import com.pedroapps.smartring20.database.SmartRingEntity

data class SmartRingUI(
    val address: String,
    val ringName: String
)  {


    fun toSmartRingEntity() : SmartRingEntity {
        return SmartRingEntity(
            address = address,
            ringName = ringName
        )
    }

    companion object {
        fun emptySmartRingUI() : SmartRingUI {
            return SmartRingUI(
                address = "",
                ringName = ""
            )
        }
    }

}
