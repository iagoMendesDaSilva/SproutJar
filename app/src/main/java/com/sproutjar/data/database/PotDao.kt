package com.sproutjar.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sproutjar.data.models.Pot

@Dao
interface PotDao {

    @Query("SELECT * FROM pots WHERE id=:id")
    suspend fun getPot(id: Int): Pot

    @Query("SELECT * FROM pots")
    suspend fun getPots(): List<Pot>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPot(pot: Pot)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePot(pot: Pot)

    @Delete
    suspend fun deletePot(pot: Pot)

}