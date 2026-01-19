package com.example.gamingsanta

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gamingsanta.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    // ViewBinding dává přístup k prvkům z activity_main.xml bez findViewById.
    private lateinit var binding: ActivityMainBinding

    // lastIndex si pamatuje posledně vylosovanou hru, aby se neopakovala.
    private var lastIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binding se inicializuje a nastaví se jako obsah obrazovky.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Seznam her se načte z res/values/arrays.xml jako string-array "games".
        val games = resources.getStringArray(R.array.games)

        // Kliknutí na START vylosuje náhodnou hru a vypíše ji pod tlačítkem.
        binding.btnRoll.setOnClickListener {

            // Když je seznam prázdný, nic se neděje.
            if (games.isEmpty()) return@setOnClickListener

            // Náhodně vybereme index ze seznamu her.
            var randomIndex = Random.nextInt(games.size)

            // Když je her víc, tak se snažíme nevybrat stejnou jako minule.
            if (games.size > 1) {
                while (randomIndex == lastIndex) {
                    randomIndex = Random.nextInt(games.size)
                }
            }

            // Uložíme si vybraný index jako poslední.
            lastIndex = randomIndex

            // Vypíšeme vybranou hru do textového pole.
            binding.textViewGame.text = games[randomIndex]
        }
    }
}