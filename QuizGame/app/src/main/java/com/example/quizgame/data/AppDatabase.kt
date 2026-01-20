package com.example.quizgame.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Question::class, Result::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // DAO pro práci s otázkami.
    abstract fun questionDao(): QuestionDao

    // DAO pro práci s výsledky.
    abstract fun resultDao(): ResultDao

    companion object {

        // Singleton instance databáze.
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE != null) return INSTANCE!!

            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "quiz_db"
            ).addCallback(object : Callback() {

                // Seed se spustí při prvním vytvoření databáze.
                override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                    super.onCreate(db)

                    CoroutineScope(Dispatchers.IO).launch {
                        val database = INSTANCE ?: return@launch
                        val dao = database.questionDao()

                        // Seed vloží otázky jen když je tabulka prázdná.
                        if (dao.count() == 0) {
                            dao.insertAll(seedMcuQuestions())
                        }
                    }
                }
            }).build()

            return INSTANCE!!
        }

        // Seed otázky jsou oddělené do funkce kvůli přehlednosti.
        private fun seedMcuQuestions(): List<Question> {
            return listOf(
                Question(
                    text = "Kdo v Endgame použije Infinity Gauntlet a vrátí vymazané lidi?",
                    a = "Thor",
                    b = "Hulk",
                    c = "Iron Man",
                    d = "Captain America",
                    correctIndex = 1
                ),
                Question(
                    text = "Kdo v Endgame nakonec lusknutím porazí Thanose a jeho armádu?",
                    a = "Doctor Strange",
                    b = "Scarlet Witch",
                    c = "Iron Man",
                    d = "Captain Marvel",
                    correctIndex = 2
                ),
                Question(
                    text = "Která postava v Endgame získá zpět Soul Stone výměnou za oběť?",
                    a = "Black Widow",
                    b = "Hawkeye",
                    c = "Gamora",
                    d = "Nebula",
                    correctIndex = 0
                ),
                Question(
                    text = "Kde se odehraje oběť pro získání Soul Stone?",
                    a = "Titan",
                    b = "Asgard",
                    c = "Vormir",
                    d = "Knowhere",
                    correctIndex = 2
                ),
                Question(
                    text = "Jak se jmenuje Thorova sekera vytvořená v Infinity War?",
                    a = "Mjolnir",
                    b = "Stormbreaker",
                    c = "Gungnir",
                    d = "Hofund",
                    correctIndex = 1
                ),
                Question(
                    text = "Který kámen měl Loki ukrytý v žezle v prvních Avengers?",
                    a = "Time Stone",
                    b = "Mind Stone",
                    c = "Space Stone",
                    d = "Reality Stone",
                    correctIndex = 1
                ),
                Question(
                    text = "Co je skutečnou identitou Zimojeda v Captain America The Winter Soldier?",
                    a = "Sam Wilson",
                    b = "James Bucky Barnes",
                    c = "John Walker",
                    d = "Nick Fury",
                    correctIndex = 1
                ),
                Question(
                    text = "Kdo zabije Mysterio ve filmu Spider-Man Far From Home?",
                    a = "Spider-Man",
                    b = "Nick Fury",
                    c = "Mysterio nezemře",
                    d = "Happy Hogan",
                    correctIndex = 2
                ),
                Question(
                    text = "Kdo je hlavním záporákem ve filmu Doctor Strange in the Multiverse of Madness?",
                    a = "Wanda Maximoff",
                    b = "Kaecilius",
                    c = "Dormammu",
                    d = "Baron Mordo",
                    correctIndex = 0
                ),
                Question(
                    text = "Jak se jmenuje město, které Wanda vytvoří jako falešnou realitu ve WandaVision?",
                    a = "Sokovia",
                    b = "Westview",
                    c = "New Asgard",
                    d = "Kamar-Taj",
                    correctIndex = 1
                ),
                Question(
                    text = "Co je název organizace vedené He Who Remains v seriálu Loki?",
                    a = "SWORD",
                    b = "TVA",
                    c = "AIM",
                    d = "SHIELD",
                    correctIndex = 1
                ),
                Question(
                    text = "Který Guardian se obětuje v Guardians of the Galaxy Vol. 2?",
                    a = "Rocket",
                    b = "Drax",
                    c = "Yondu",
                    d = "Groot",
                    correctIndex = 2
                ),
                Question(
                    text = "Jak se jmenuje planeta, kde se Avengers utkají s Thanosem v Infinity War?",
                    a = "Xandar",
                    b = "Titan",
                    c = "Sakaar",
                    d = "Ego",
                    correctIndex = 1
                ),
                Question(
                    text = "Kdo je vrahem TChally v Black Panther Wakanda Forever?",
                    a = "Namor",
                    b = "Killmonger",
                    c = "TChalla nezemře na vraždu",
                    d = "Okoye",
                    correctIndex = 2
                ),
                Question(
                    text = "Kdo v No Way Home způsobí, že si svět přestane pamatovat Petera Parkera?",
                    a = "Doctor Strange",
                    b = "Green Goblin",
                    c = "MJ",
                    d = "Ned",
                    correctIndex = 0
                )
            )
        }
    }
}
