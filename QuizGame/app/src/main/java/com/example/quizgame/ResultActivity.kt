package com.example.quizgame

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizgame.data.AppDatabase
import com.example.quizgame.data.Result
import com.example.quizgame.data.PlayerPrefs
import com.example.quizgame.databinding.ActivityResultsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResultsActivity : AppCompatActivity() {

    // ViewBinding dává přístup k prvkům z activity_results.xml bez findViewById.
    private lateinit var binding: ActivityResultsBinding

    // Adapter vykresluje výsledky jako karty v RecyclerView.
    private val adapter = ResultsAdapter { result ->
        showDeleteDialog(result)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binding se inicializuje a nastaví se jako obsah obrazovky.
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView používá svislý seznam karet.
        binding.recyclerViewResults.layoutManager = LinearLayoutManager(this)

        // RecyclerView dostane adapter, který umí vykreslit karty.
        binding.recyclerViewResults.adapter = adapter

        // Kliknutí na hrát znovu otevře MainActivity kvůli novému zadání jména.
        binding.btnPlayAgain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        // Výsledky načteme z databáze pro aktuální jméno hráče.
        loadResults()
    }

    private fun loadResults() {
        // Jméno hráče se načte z preferencí.
        val name = PlayerPrefs.loadName(this)

        // Výsledky načítáme na IO vlákně, aby se UI nezaseklo.
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@ResultsActivity)
            val results = db.resultDao().getByPlayer(name)

            // Na Main vlákně vykreslíme poslední výsledek a seznam karet.
            withContext(Dispatchers.Main) {
                val last = results.firstOrNull()
                binding.textViewLastResult.text =
                    if (last != null) "Poslední výsledek: ${last.correctCount}/${last.totalCount}  (${last.playerName})"
                    else "Zatím nemáš žádný výsledek."

                adapter.setData(results)
            }
        }
    }

    private fun showDeleteDialog(result: Result) {
        // Dialog se zeptá uživatele, jestli chce výsledek opravdu smazat.
        AlertDialog.Builder(this)
            .setTitle("Smazat výsledek")
            .setMessage("Opravdu chcete smazat výsledek hráče ${result.playerName}?")
            .setPositiveButton("Ano") { _, _ ->
                deleteResult(result)
            }
            .setNegativeButton("Ne", null)
            .show()
    }

    private fun deleteResult(result: Result) {
        // Mazání z databáze se provádí na IO vlákně.
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@ResultsActivity)
            db.resultDao().delete(result)

            // Po smazání načteme znovu data, aby se seznam aktualizoval.
            withContext(Dispatchers.Main) {
                loadResults()
            }
        }
    }
}