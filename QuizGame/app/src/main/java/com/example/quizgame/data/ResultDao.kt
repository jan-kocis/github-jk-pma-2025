package com.example.quizgame.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete

@Dao
interface ResultDao {

    @Insert
    suspend fun insert(result: Result)

    @Query("SELECT * FROM result_table WHERE playerName = :name ORDER BY timestamp DESC")
    suspend fun getByPlayer(name: String): List<Result>

    // Smaže konkrétní výsledek podle jeho ID.
    @Delete
    suspend fun delete(result: Result)
}