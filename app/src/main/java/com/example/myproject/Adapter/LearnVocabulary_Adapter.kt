package com.example.myproject.Adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myproject.Model.Topic
import com.example.myproject.Model.User
import com.example.myproject.R

class LearnVocabulary_Adapter :
    ListAdapter<Topic, LearnVocabulary_Adapter.LearVocabularyViewHolder>(TopicDiffCallback) {

    var onItemClick: ((Int) -> Unit)? = null

    inner class LearVocabularyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtLessonTitle: TextView = itemView.findViewById(R.id.lesson_title)
        val txtDescription: TextView = itemView.findViewById(R.id.lesson_description)
        val imgLesson: ImageView = itemView.findViewById(R.id.lesson_image)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LearVocabularyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lesson, parent, false)
        return LearVocabularyViewHolder(view)
    }

    override fun onBindViewHolder(holder: LearVocabularyViewHolder, position: Int) {
        val topic = getItem(position)
        holder.txtLessonTitle.text = topic.NameTopic
        holder.txtDescription.text = topic.description

        Glide.with(holder.itemView.context)
            .load(topic.imageUrl)
            .centerCrop()
            .error(R.drawable.avatar)
            .into(holder.imgLesson)
    }


    // DiffUtil để so sánh và cập nhật danh sách hiệu quả
    object TopicDiffCallback : DiffUtil.ItemCallback<Topic>() {
        override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean {
            return oldItem == newItem
        }
    }
}
