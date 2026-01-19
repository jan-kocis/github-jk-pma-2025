package com.example.poutokrve

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.poutokrve.data.Character
import com.example.poutokrve.data.CharacterDatabaseInstance
import com.example.poutokrve.databinding.ActivityActorListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActorListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActorListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityActorListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Tlačítko zpět, které nás vrátí na hlavní stránku
        binding.btnBackCircle.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        //Přístup k databázi a DAO skrz které se volají dotazy
        val db = CharacterDatabaseInstance.getDatabase(this)
        val characterDao = db.characterDao()

        //Adapter s callbacky pro edit / delete
        val adapter = CharacterAdapter(
            onEdit = { character ->

                // EDIT otevře AddActorActivity v "edit módu" za pomocí ID existujícího záznamu
                val intent = Intent(this, AddActorActivity::class.java)
                intent.putExtra("ID", character.id)
                startActivity(intent)
            },
            onDelete = { character ->

                // DELETE zobrazí potvrzovací dialog pro smazání záznamu
                showDeleteDialog(character, characterDao)
            }
        )

        //Nastavení RecyclerView, tedy položek, které se zobrazí pod sebou jako seznam
        binding.recyclerViewActors.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewActors.adapter = adapter

        //Načtení veškerých dat z databáze
        CoroutineScope(Dispatchers.Main).launch {
            characterDao.getAllCharacters().collect { characters ->
                adapter.setData(characters)
            }
        }
    }

    //Potvrzovací dialog pro smazání záznamu
    private fun showDeleteDialog(character: Character, characterDao: com.example.poutokrve.data.CharacterDao) {
        AlertDialog.Builder(this)
            .setTitle("Smazat záznam")
            .setMessage("Opravdu si přejete odebrat ${character.firstName} ${character.lastName}?")
            .setPositiveButton("Ano") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    characterDao.delete(character)
                }
            }
            .setNegativeButton("Ne", null)
            .show()
    }
}