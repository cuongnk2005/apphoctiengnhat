package com.example.myproject.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myproject.Model.Topic
import com.example.myproject.R

class Home_Adapter:RecyclerView.Adapter<Home_Adapter.HomeViewHolder>(){
    private var listTopic: List<Topic> = ArrayList();
    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtlessontitle: TextView = itemView.findViewById(R.id.lesson_title)
        val txtDescipition: TextView = itemView.findViewById(R.id.lesson_description)
        val imgLesson: ImageView = itemView.findViewById(R.id.lesson_image)
    }
    var onItemClick: ((Int) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lesson, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        var topic = listTopic[position]
        holder.txtlessontitle.text = topic.NameTopic
        holder.txtDescipition.text = topic.description
        Glide.with(holder.itemView.context)
            .load(topic.imageUrl)
            .centerCrop()
            .error(R.drawable.avatar)
            .into(holder.imgLesson)
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