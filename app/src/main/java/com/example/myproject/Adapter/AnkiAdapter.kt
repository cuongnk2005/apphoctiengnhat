package com.example.myproject.Adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.R

class AnkiAdapter:RecyclerView.Adapter<AnkiAdapter.AnkiViewHolder>(){
    private var listAnki = mutableListOf<String>()
    private var listValue = mutableListOf<List<Int>>()
    // lop long co the truy cap thuoc tinh cua lop ben ngoai
    inner class AnkiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtlessontitle: TextView = itemView.findViewById(R.id.lesson_title)
        val txtnumBlue: TextView = itemView.findViewById(R.id.numBlue)
        val txtnumRed: TextView = itemView.findViewById(R.id.numRed)
        val txtnumGreen: TextView = itemView.findViewById(R.id.numGreen)
    }
    var onItemClick: ((Int) -> Unit)? = null
    var onItemDelete: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnkiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_anki, parent, false)
        return AnkiViewHolder(view)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AnkiViewHolder, position: Int) {
        var name = listAnki[position]
        holder.txtlessontitle.text = name
        if (listValue.size - 1 >= position) {
            if (listValue[position].size == 3) {
                Log.d("cochafj", "co chay du null")
                var numBlue = listValue[position][0]
                var numRed = listValue[position][1]
                var numGreen = listValue[position][2]
                numBlue?.let {
                    holder.txtnumBlue.text = it.toString()
                }
                numRed?.let {
                    holder.txtnumRed.text = it.toString()
                }
                numGreen?.let {
                    holder.txtnumGreen.text = it.toString()
                }
            }
        }



            holder.itemView.setOnClickListener {
                onItemClick?.invoke(position)
            }
        }

    override fun getItemCount(): Int {
        return listAnki.size
    }
    fun updateBo(newTopics: List<String> ) {
        listAnki = newTopics.toMutableList()

        notifyDataSetChanged()
    }
    fun UpdateValue(newListValue: List<List<Int>>) {
        if (newListValue != null) {
            listValue = newListValue.toMutableList()
            notifyDataSetChanged()
        }
    }

    fun deleteItem(position: Int): String {
        val delete = listAnki[position]
        listAnki.removeAt(position)
        notifyItemRemoved(position)
        onItemDelete?.invoke(delete)
        return delete
    }
}