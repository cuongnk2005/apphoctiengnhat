package com.example.myproject.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.Model.Topic
import com.example.myproject.R

class Home_Adapter:RecyclerView.Adapter<Home_Adapter.HomeViewHolder>(){
    private var listTopic: List<Topic> = ArrayList();
    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtlessontitle: TextView = itemView.findViewById(R.id.lesson_title)
        val txtDescipition: TextView = itemView.findViewById(R.id.lesson_description)
    }
<<<<<<< HEAD
    var onItemClick: ((Int) -> Unit)? = null
=======
  var onItemClick: ((Int) -> Unit)? = null
>>>>>>> 58ad0257e018e0ba0616747d581ce0f1b0dec4d8
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lesson, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        var topic = listTopic[position]
        holder.txtlessontitle.text = topic.NameTopic
        holder.txtDescipition.text = topic.description
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position)

        }
    }
    override fun getItemCount(): Int {
        return listTopic.size
    }
    fun updateData(newTopics: List<Topic>) {
        listTopic = newTopics
        notifyDataSetChanged()
    }
}