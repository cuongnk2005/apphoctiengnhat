package com.example.myproject.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cloudinary.android.callback.UploadCallback
import com.example.myproject.Model.Topic
import com.example.myproject.R

class LearnVocabulary_Adapter:RecyclerView.Adapter<LearnVocabulary_Adapter.LearVocabularyViewHolder>(){
    private var listTopic: List<Topic> = ArrayList();
    inner class LearVocabularyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtlessontitle: TextView = itemView.findViewById(R.id.lesson_title)
        val txtDescipition: TextView = itemView.findViewById(R.id.lesson_description)
    }
  var onItemClick: ((Int) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LearVocabularyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lesson, parent, false)
        return LearVocabularyViewHolder(view)
    }

    override fun onBindViewHolder(holder: LearVocabularyViewHolder, position: Int) {
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