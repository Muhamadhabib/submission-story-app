package com.demuha.submission01.view.story

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.demuha.submission01.databinding.ActivityStoryDetailBinding
import com.demuha.submission01.view.home.MainActivity

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

//        val id = intent.getStringExtra(MainActivity.EXTRA_ID)
        val name = intent.getStringExtra(MainActivity.EXTRA_NAME)
        val description = intent.getStringExtra(MainActivity.EXTRA_DESC)
        val photo = intent.getStringExtra(MainActivity.EXTRA_PHOTO)
        val createdAt = intent.getStringExtra(MainActivity.EXTRA_CREATED)

        binding.apply {
            tvDetailName.text = name
            tvDetailDate.text = createdAt
            tvDetailDesc.text = description
            Glide.with(this@StoryDetailActivity)
                .load(photo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivDetailPhoto)
        }
    }
}