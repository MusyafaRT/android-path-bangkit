package com.example.storyapp.view.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.storyapp.data.api.ListStoryItem
import com.example.storyapp.databinding.ActivityDetailStoryBinding
import com.example.storyapp.view.story.StoryActivity.Companion.EXTRA_DATA

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setupData()

    }

    private fun setupView(){
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupData(){
        val data = intent.getParcelableExtra<ListStoryItem>(EXTRA_DATA) as ListStoryItem
        binding.apply {
            tvItemTitle.text = data.name
            tvItemDescription.text = data.description
            Glide.with(this@DetailStoryActivity)
                .load(data.photoUrl)
                .into(imgPoster)
        }
    }

}