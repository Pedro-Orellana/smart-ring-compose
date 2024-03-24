package com.pedroapps.smartring20.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SmartRingEntity::class], version = 1, exportSchema = false)
abstract class SmartRingDataBase : RoomDatabase() {
    abstract fun smartRingDAO() : SmartRingDAO

    companion object {

        private var instance: SmartRingDataBase? = null
        fun getInstance(application: Application) : SmartRingDataBase {

            return instance ?: synchronized(this) {
               val database =  Room.databaseBuilder(application,
                    SmartRingDataBase::class.java,
                    "smart_ring_data_base")
                    .fallbackToDestructiveMigration()
                    .build()
                instance = database
                database
            }
        }
    }
}