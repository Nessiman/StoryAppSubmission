package com.example.storyappsubmission.view.main

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION_CODES
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyappsubmission.databinding.StoryItemBinding
import com.example.storyappsubmission.view.DateConverter
import com.example.storyappsubmission.view.story.ListStoryItem
import com.example.storyappsubmission.view.story.StoryDetailActivity

class MainAdapter(private var storyList: List<ListStoryItem>) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    class ViewHolder(var binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return storyList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val storyItem = storyList[position]

        Glide.with(holder.itemView.context)
            .load(storyItem.photoUrl)
            .into(holder.binding.photo)

        holder.binding.username.text = storyItem.name
        holder.binding.description.text = storyItem.description
        if (storyItem.createdAt != null) {
            holder.binding.date.text = DateConverter(storyItem.createdAt)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, StoryDetailActivity::class.java)
            intent.putExtra("id", storyItem.id)
            holder.itemView.context.startActivity(intent)
        }
    }
}