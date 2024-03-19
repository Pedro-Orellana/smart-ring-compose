package com.pedroapps.smartring20.database

import androidx.room.Entity

@Entity(tableName = "smart_ring")
data class SmartRingEntity(
    val name: String
)
