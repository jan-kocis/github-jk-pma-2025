package com.example.quizgame

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizgame.data.PlayerPrefs
import com.example.quizgame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binding se inicializuje a nastaví se jako obsah obrazovky.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Do inputu předvyplníme uložené jméno, pokud existuje.
        binding.editTextName.setText(PlayerPrefs.loadName(this))

        // Kliknutí na Start uloží jméno a otevře GameActivity.
        binding.btnStart.setOnClickListener {
            val name = binding.editTextName.text.toString().trim()

            // Prázdné jméno nedovolíme, protože identita je povinná.
            if (name.isEmpty()) {
                Toast.makeText(this, "Zadej jméno.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Jméno si uložíme, aby se příště nemuselo psát znovu.
            PlayerPrefs.saveName(this, name)

            // Hru spustíme přechodem na GameActivity.
            startActivity(Intent(this, GameActivity::class.java))
        }
    }
}