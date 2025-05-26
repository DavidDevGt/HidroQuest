package com.daviddevgt.hidroplus.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WaterRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val glasses: Int = 0,
    val goalReached: Boolean = false
)
