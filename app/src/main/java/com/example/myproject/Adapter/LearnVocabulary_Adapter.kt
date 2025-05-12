package com.example.myproject.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myproject.Model.OldTopic
import com.example.myproject.Model.Topic
import com.example.myproject.R

class LearnVocabulary_Adapter() : RecyclerView.Adapter<LearnVocabulary_Adapter.LearVocabularyViewHolder>() {
    private var topicList =  ArrayList<Topic>()
    private var listOldTopic = ArrayList<OldTopic>()
    var onItemClick: ((Int) -> Unit)? = null

    inner class LearVocabularyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtLessonTitle: TextView = itemView.findViewById(R.id.lesson_title)
        val txtDescription: TextView = itemView.findViewById(R.id.lesson_description)
        val imgLesson: ImageView = itemView.findViewById(R.id.lesson_image)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LearVocabularyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lesson, parent, false)
        return LearVocabularyViewHolder(view)
    }

    override fun onBindViewHolder(holder: LearVocabularyViewHolder, position: Int) {
        val topic = topicList[position]
        holder.txtLessonTitle.text = topic.NameTopic
        holder.txtDescription.text = topic.description

        Glide.with(holder.itemView.context)
            .load(topic.imageUrl)
            .centerCrop()
            .error(R.drawable.avatar)
            .into(holder.imgLesson)

        val exists = listOldTopic.any { it.id == topic.id && it.status == true }
        val cardView = holder.itemView.findViewById<CardView>(R.id.item_card)
        if (exists) {


            cardView.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.primaryColor)
            )
        } else{
            cardView.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.white)
            )
        }
    }

    override fun getItemCount(): Int = topicList.size

    // Hàm cập nhật dữ liệu khi cần
    fun updateData(newList: List<Topic>, newOldList: ArrayList<OldTopic>) {
        topicList.clear()
        topicList.addAll(newList)
        listOldTopic = newOldList
        notifyDataSetChanged()
    }
}
