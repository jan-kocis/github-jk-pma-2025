package com.example.quizgame.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "result_table")
data class Result(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val playerName: String,
    val timestamp: Long,
    val correctCount: Int,
    val totalCount: Int
)