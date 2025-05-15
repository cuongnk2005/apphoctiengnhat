package com.example.myproject.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.R

class AnkiAdapter:RecyclerView.Adapter<AnkiAdapter.AnkiViewHolder>(){
    private var listAnki = mutableListOf<String>()
    inner class AnkiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtlessontitle: TextView = itemView.findViewById(R.id.lesson_title)
    }
    var onItemClick: ((Int) -> Unit)? = null
    var onItemDelete: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnkiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_anki, parent, false)
        return AnkiViewHolder(view)
    }
    override fun onBindViewHolder(holder: AnkiViewHolder, position: Int) {
        var name = listAnki[position]
        holder.txtlessontitle.text = name
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position)
        }
    }
    override fun getItemCount(): Int {
        return listAnki.size
    }
    fun updateData(newTopics: List<String>) {
        listAnki = newTopics.toMutableList()
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int): String {
        val delete = listAnki[position]
        listAnki.removeAt(position)
        notifyItemRemoved(position)
        onItemDelete?.invoke(delete)
        return delete
    }
}