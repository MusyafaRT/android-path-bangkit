package com.example.storyapp.view.story


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.data.api.ListStoryItem
import com.example.storyapp.databinding.ActivityStoryBinding
import com.example.storyapp.view.ViewModelFactory
import com.example.storyapp.view.addstory.StoryAddActivity
import com.example.storyapp.view.main.MainActivity


class StoryActivity : AppCompatActivity() {

    private lateinit var storyBinding: ActivityStoryBinding
    private val storyListViewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storyBinding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(storyBinding.root)

        storyListViewModel.listStories.observe(this) { list ->
            setStoryList(list)
        }

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        storyBinding.rvStory.layoutManager = layoutManager

        storyBinding.fabAdd.setOnClickListener {
            val intent = Intent(this@StoryActivity, StoryAddActivity::class.java)
            startActivity(intent)
        }

        storyBinding.topAppBar.setOnMenuItemClickListener{
                menuItem -> when(menuItem.itemId) {
            R.id.menu -> {
                val intent = Intent(this@StoryActivity, MainActivity::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
        }


    }

    override fun onResume() {
        super.onResume()
        storyListViewModel.getSession().observe(this){user ->
            if(user.isLogin){
                token = user.token
            }
        }
        storyListViewModel.getAllList(token)

    }


    private fun setStoryList(stories: List<ListStoryItem>) {
        val adapter = StoryAdapter()
        adapter.submitList(stories)
        adapter.setOnItemClickCallback(object: StoryAdapter.OnItemClickCallback{
            override fun onItemClick(story: ListStoryItem) {
                val detailIntent = Intent(this@StoryActivity, DetailStoryActivity::class.java)
                detailIntent.putExtra(EXTRA_DATA, story)
                startActivity(detailIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@StoryActivity as Activity).toBundle())
            }
        })
        storyBinding.rvStory.adapter = adapter
    }

    companion object{
        const val EXTRA_DATA = "extra_data"
    }
}