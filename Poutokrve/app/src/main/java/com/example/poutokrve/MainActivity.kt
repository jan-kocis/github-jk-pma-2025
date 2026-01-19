package com.example.poutokrve

import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.poutokrve.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Tlačítko: otevře obrazovku pro přidání nebo edit dabéra
        binding.btnAddActor.setOnClickListener {
            startActivity(Intent(this, AddActorActivity::class.java))
        }

        //Tlačítko: otevře obrazovku se seznamem dabérů
        binding.btnActorList.setOnClickListener {
            startActivity(Intent(this, ActorListActivity::class.java))
        }

    }
}