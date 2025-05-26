package com.daviddevgt.hidroplus.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WaterRecord::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun waterDao(): WaterDao
}