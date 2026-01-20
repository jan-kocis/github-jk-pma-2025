package com.example.quizgame.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_table")
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val a: String,
    val b: String,
    val c: String,
    val d: String,
    val correctIndex: Int
)