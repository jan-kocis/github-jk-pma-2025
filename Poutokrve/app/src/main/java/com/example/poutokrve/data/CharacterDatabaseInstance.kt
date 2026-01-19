package com.example.poutokrve.data

import android.content.Context
import androidx.room.Room

object CharacterDatabaseInstance {

    @Volatile
    private var INSTANCE: CharacterDatabase? = null


    //Vrátí instanci databáze a pokud ještě neexistuje, tak jí vytvoří.
    fun getDatabase(context: Context): CharacterDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                CharacterDatabase::class.java,
                "characterhub_database"
            )
                .build()
            INSTANCE = instance
            instance
        }
    }
}