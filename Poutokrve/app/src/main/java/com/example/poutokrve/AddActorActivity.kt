package com.example.poutokrve

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.poutokrve.data.Character
import com.example.poutokrve.data.CharacterDatabaseInstance
import com.example.poutokrve.databinding.ActivityAddActorBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import android.widget.ArrayAdapter
import com.example.poutokrve.R

import android.net.Uri
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts

class AddActorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddActorBinding
    private var editId: Int? = null
    private var selectedImageUri: Uri? = null
    private var loadedImageUriString: String? = null

    private val pickImage = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            selectedImageUri = uri
            binding.imageViewPreview.setImageURI(uri)
        }
    }

    //Přidání záznamu do databáze
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddActorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Zpět na domovskou stránku
        binding.btnBackCircle.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        //DB + DAO
        val db = CharacterDatabaseInstance.getDatabase(this)
        val CharacterDao = db.characterDao()

        //Načtení spinneru lokacemi
        val locations = resources.getStringArray(R.array.locations)

        val spinnerAdapter  = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            locations
        )

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerLocation.adapter = spinnerAdapter

        //Kliknutím na PLUS se otevře systémový výběr obrázku
        binding.btnPickImageCircle.setOnClickListener {
            pickImage.launch(arrayOf("image/*"))
        }

        //Zjištění, zda přidáváme nebo upravujeme záznam podle ID
        val idFromIntent = intent.getIntExtra("ID", -1)
        if (idFromIntent != -1) {
            editId = idFromIntent
            binding.btnSaveActor.text = "Uložit změny"

            //Načtení existujícího záznamu z DB podle ID a naplní se formulář daty
            CoroutineScope(Dispatchers.IO).launch {
                val character = CharacterDao.getCharacterById(idFromIntent) ?: return@launch

                runOnUiThread {

                    //texty
                    binding.editTextFirstName.setText(character.firstName)
                    binding.editTextLastName.setText(character.lastName)
                    binding.editTextCharacterName.setText(character.characterName)
                    binding.editTextDescription.setText(character.description)

                    //spinner
                    val idx = locations.indexOf(character.location).coerceAtLeast(0)
                    binding.spinnerLocation.setSelection(idx)

                    //obrázek
                    loadedImageUriString = character.imageUri
                    if (!character.imageUri.isNullOrEmpty()) {
                        selectedImageUri = Uri.parse(character.imageUri)
                        binding.imageViewPreview.setImageURI(selectedImageUri)
                    }
                }
            }
        }

        //Uložení záznamu při editu i přidávání
        binding.btnSaveActor.setOnClickListener {

            val firstName = binding.editTextFirstName.text.toString()
            val lastName = binding.editTextLastName.text.toString()
            val characterName = binding.editTextCharacterName.text.toString()
            val location = binding.spinnerLocation.selectedItem.toString()
            val description = binding.editTextDescription.text.toString()

            if (firstName.isEmpty() || lastName.isEmpty() || characterName.isEmpty()) {
                Toast.makeText(this, "Vyplňte prázdné pole!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val finalImageUri = selectedImageUri?.toString() ?: loadedImageUriString

            CoroutineScope(Dispatchers.IO).launch {
                if (editId == null) {
                    // PŘIDAT
                    val character = Character(
                        firstName = firstName,
                        lastName = lastName,
                        characterName = characterName,
                        location = location,
                        description = description,
                        imageUri = finalImageUri
                    )
                    CharacterDao.insert(character)
                } else {
                    // UPRAVIT
                    val character = Character(
                        firstName = firstName,
                        lastName = lastName,
                        characterName = characterName,
                        location = location,
                        description = description,
                        imageUri = finalImageUri
                    )
                    CharacterDao.update(character)
                }
                finish()

            }
        }
    }
}