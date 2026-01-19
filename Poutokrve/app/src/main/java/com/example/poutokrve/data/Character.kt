package com.example.poutokrve.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class Character(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val characterName: String,
    val location: String,
    val description: String,
    val imageUri: String?
)
