package com.example.poutokrve

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.poutokrve.data.Character
import com.example.poutokrve.databinding.ItemActorBinding

class CharacterAdapter(

    //Callback co se má stát, když se klikne na ikonku upravit nebo smazat
    private val onEdit: (Character) -> Unit,
    private val onDelete: (Character) -> Unit
) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    //Aktuální data pro zobrazení v seznamu
    private var character: List<Character> = emptyList()

    //Zavolá seznam pokaždé, když se objeví nový záznam
    fun setData(newData: List<Character>) {
        character = newData
        notifyDataSetChanged()
    }

    class CharacterViewHolder(val binding: ItemActorBinding) : RecyclerView.ViewHolder(binding.root)

    //Volá se, když je potřeba vytvořit nový řádek se záznamem
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ItemActorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    //Volá se pro naplnění každého řádku příslušnými daty
    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val item = character[position]

        holder.binding.textViewName.text = "${item.firstName} ${item.lastName}"
        holder.binding.textViewCharacter.text = item.characterName
        holder.binding.textViewLocation.text = item.location
        holder.binding.textViewDescription.text = item.description

        if (item.imageUri.isNullOrEmpty()) {
            holder.binding.imageViewItem.setImageDrawable(null)
        } else {
            holder.binding.imageViewItem.setImageURI(Uri.parse(item.imageUri))
        }

        holder.binding.btnEditCircle.setOnClickListener { onEdit(item) }
        holder.binding.btnDeleteCircle.setOnClickListener { onDelete(item) }
    }

    //Řádky, které je potřeba vykreslit = počet položek v seznamu
    override fun getItemCount(): Int = character.size
}