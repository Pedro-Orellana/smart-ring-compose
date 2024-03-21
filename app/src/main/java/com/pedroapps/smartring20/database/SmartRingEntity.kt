package com.pedroapps.smartring20.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pedroapps.smartring20.viewmodels.SmartRingUI

@Entity(tableName = "smart_ring")
data class SmartRingEntity(
    @PrimaryKey
    @ColumnInfo(name = "address_column")
    val address: String,
    @ColumnInfo(name = "ring_name_column")
    val ringName: String

) {

    fun toSmartRingUI(): SmartRingUI {

        return SmartRingUI(
            address = address,
            ringName = ringName
        )
    }

}
