package com.example.quizgame

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizgame.data.Result
import com.example.quizgame.databinding.ItemResultBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultsAdapter(
    private val onDelete: (Result) -> Unit
) : RecyclerView.Adapter<ResultsAdapter.ResultViewHolder>() {

    // data drží výsledky, které se mají vykreslit v seznamu.
    private var data: List<Result> = emptyList()

    // sdf převádí timestamp na čitelný text.
    private val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    // setData přepíše data a řekne RecyclerView, že se má překreslit.
    fun setData(items: List<Result>) {
        data = items
        notifyDataSetChanged()
    }

    // ViewHolder drží binding pro jednu kartu item_result.xml.
    class ResultViewHolder(val binding: ItemResultBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        // Binding nafoukne layout karty a připraví ho pro RecyclerView.
        val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val item = data[position]

        // Jméno hráče se vypíše na kartu.
        holder.binding.textViewPlayer.text = "Hráč: ${item.playerName}"

        // Skóre se vypíše na kartu.
        holder.binding.textViewScore.text = "Skóre: ${item.correctCount}/${item.totalCount}"

        // Datum se převede z timestamp a vypíše na kartu.
        holder.binding.textViewDate.text = "Datum: ${sdf.format(Date(item.timestamp))}"

        // Kliknutí na delete tlačítko zavolá callback s konkrétním výsledkem.
        holder.binding.btnDeleteCircle.setOnClickListener {
            onDelete(item)
        }
    }

    override fun getItemCount(): Int = data.size
}