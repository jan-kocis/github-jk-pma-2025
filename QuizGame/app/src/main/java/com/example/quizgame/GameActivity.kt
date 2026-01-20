package com.example.quizgame

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizgame.data.AppDatabase
import com.example.quizgame.data.Question
import com.example.quizgame.data.Result
import com.example.quizgame.data.PlayerPrefs
import com.example.quizgame.databinding.ActivityGameBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    // questions drží otázky načtené z Room databáze.
    private var questions: List<Question> = emptyList()

    // currentIndex říká, která otázka se právě zobrazuje.
    private var currentIndex: Int = 0

    // score drží počet správných odpovědí.
    private var score: Int = 0

    // totalCount určuje kolik otázek se bude hrát z celé databáze.
    private val totalCount: Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binding se inicializuje a nastaví se jako obsah obrazovky.
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Otázky načteme z Room databáze a připravíme hru.
        loadQuestions()

        // Kliky na odpovědi volají jednotnou funkci s indexem odpovědi.
        binding.btnA.setOnClickListener { answer(0) }
        binding.btnB.setOnClickListener { answer(1) }
        binding.btnC.setOnClickListener { answer(2) }
        binding.btnD.setOnClickListener { answer(3) }
    }

    private fun loadQuestions() {
        // Databázi načítáme na IO vlákně, aby se UI nezaseklo.
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@GameActivity)
            val all = db.questionDao().getAll()

            // Když v DB nejsou otázky, hra se nedá spustit.
            if (all.isEmpty()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@GameActivity, "V databázi nejsou otázky.", Toast.LENGTH_SHORT).show()
                    finish()
                }
                return@launch
            }

            // Zamícháme otázky a vezmeme jen tolik, kolik chceme odehrát.
            questions = all.shuffled().take(minOf(totalCount, all.size))

            // UI aktualizujeme na Main vlákně.
            withContext(Dispatchers.Main) {
                currentIndex = 0
                score = 0
                showQuestion()
            }
        }
    }

    private fun showQuestion() {
        // currentIndex musí být v rozsahu seznamu.
        if (currentIndex !in questions.indices) return

        val q = questions[currentIndex]

        // Horní řádek ukazuje postup a skóre.
        binding.textViewTop.text = "Otázka ${currentIndex + 1}/${questions.size} | Skóre $score"

        // Text otázky se vypíše do TextView.
        binding.textViewQuestion.text = q.text

        // Texty odpovědí se nastaví do tlačítek.
        binding.btnA.text = q.a
        binding.btnB.text = q.b
        binding.btnC.text = q.c
        binding.btnD.text = q.d
    }

    private fun answer(selectedIndex: Int) {
        // currentIndex musí být v rozsahu seznamu.
        if (currentIndex !in questions.indices) return

        val q = questions[currentIndex]

        // Správnou odpověď poznáme podle correctIndex v databázi.
        if (selectedIndex == q.correctIndex) {
            score += 1
        }

        // Přesuneme se na další otázku nebo ukončíme hru.
        currentIndex += 1

        if (currentIndex >= questions.size) {
            saveResultAndFinish()
        } else {
            showQuestion()
        }
    }

    private fun saveResultAndFinish() {
        // Jméno hráče načteme ze SharedPreferences, protože identita je povinná.
        val name = PlayerPrefs.loadName(this)

        // Výsledek uložíme do Room databáze na IO vlákně.
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@GameActivity)

            db.resultDao().insert(
                Result(
                    playerName = name,
                    timestamp = System.currentTimeMillis(),
                    correctCount = score,
                    totalCount = questions.size
                )
            )

            // Po uložení přejdeme na ResultsActivity.
            withContext(Dispatchers.Main) {
                startActivity(Intent(this@GameActivity, ResultsActivity::class.java))
                finish()
            }
        }
    }
}