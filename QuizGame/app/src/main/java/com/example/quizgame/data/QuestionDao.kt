package com.example.quizgame.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface QuestionDao {

    // Vrátí všechny otázky z databáze.
    @Query("SELECT * FROM question_table")
    suspend fun getAll(): List<Question>

    // Vrátí počet otázek v databázi.
    @Query("SELECT COUNT(*) FROM question_table")
    suspend fun count(): Int

    // Vloží více otázek najednou.
    @Insert
    suspend fun insertAll(items: List<Question>)
}