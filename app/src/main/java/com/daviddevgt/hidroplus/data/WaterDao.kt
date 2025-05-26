package com.daviddevgt.hidroplus.data

import androidx.room.*

@Dao
interface WaterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: WaterRecord)

    @Update
    suspend fun update(record: WaterRecord)

    @Delete
    suspend fun delete(record: WaterRecord)

    @Query("SELECT * FROM WaterRecord ORDER BY date DESC")
    suspend fun getAllRecords(): List<WaterRecord>

    @Query("SELECT * FROM WaterRecord WHERE date = :date LIMIT 1")
    suspend fun getRecordByDate(date: String): WaterRecord?

    @Query("DELETE FROM WaterRecord")
    suspend fun clearAll()
}
