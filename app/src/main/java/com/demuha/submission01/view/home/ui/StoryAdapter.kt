package com.demuha.submission01.view.home.ui

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.demuha.submission01.R
import com.demuha.submission01.databinding.ItemStoryBinding
import com.demuha.submission01.model.StoryDto
import com.demuha.submission01.util.parseDateRelative

class StoryAdapter : PagingDataAdapter<StoryDto, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK)
{
    private var onItemClickCallback: ((StoryDto, View) -> Unit)? = null

    fun setOnItemClickCallback(callback: (StoryDto, View) -> Unit) {
        onItemClickCallback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                onItemClickCallback?.invoke(item, it)
            }
        }
    }

    class StoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(story: StoryDto) {
            binding.apply {
                tvItemName.text = story.name
                tvDescription.text = story.description
                tvDate.text = parseDateRelative(story.createdAt)
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .into(ivItemPhoto)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryDto>() {
            override fun areItemsTheSame(oldItem: StoryDto, newItem: StoryDto): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: StoryDto, newItem: StoryDto): Boolean {
                return oldItem == newItem
            }
        }
    }
}