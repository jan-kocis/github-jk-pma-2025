package com.example.poutokrve.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Character::class],
    version = 1,
    exportSchema = false
)

abstract class CharacterDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}