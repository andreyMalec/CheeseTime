package com.malec.cheesetime.service.localDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.malec.cheesetime.model.Cheese
import kotlinx.coroutines.flow.Flow

@Dao
interface CheeseDao {
    @Query("SELECT * FROM Cheese")
    fun getAll(): Flow<List<Cheese>>

    @Query("SELECT * FROM Cheese WHERE id = :id")
    fun getFlowById(id: Long): Flow<Cheese>

    @Insert(onConflict = REPLACE)
    fun insert(cheese: Cheese)

    @Update
    fun update(cheese: Cheese)

    @Query("DELETE FROM Cheese")
    fun deleteAll()

    @Query("DELETE FROM Cheese WHERE id = :id")
    fun deleteById(id: Long)
}