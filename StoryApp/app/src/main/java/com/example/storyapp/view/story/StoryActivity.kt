package com.example.storyapp.view.story


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.data.api.ListStoryItem
import com.example.storyapp.databinding.ActivityStoryBinding
import com.example.storyapp.view.ViewModelFactory
import com.example.storyapp.view.addstory.StoryAddActivity
import com.example.storyapp.view.welcome.WelcomeActivity


class StoryActivity : AppCompatActivity() {

    private lateinit var storyBinding: ActivityStoryBinding
    private val storyListViewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var token = ""
    private lateinit var adapter: StoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storyBinding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(storyBinding.root)

        setupUser()
        setupAdapter()
        setupList()
        showLoading()

        storyBinding.fabAdd.setOnClickListener {
            val intent = Intent(this@StoryActivity, StoryAddActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("StoryActivity", "onCreateOptionsMenu called")
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("StoryActivity", "onOptionsItemSelected called")
        return when (item.itemId) {
            R.id.menu -> {
                Log.d("StoryActivity", "Logout menu item clicked")
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        Log.d("StoryActivity", "Logout function called")
        storyListViewModel.logOut()
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupUser(){
        storyListViewModel.login()
        storyListViewModel.getSession().observe(this@StoryActivity) { user ->
            token = user.token
            setupList()
        }
    }

    private fun showLoading() {
        storyListViewModel.isLoading.observe(this@StoryActivity) {
            storyBinding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun setupList(){
        storyBinding.apply {
            storyListViewModel.listStories.observe(this@StoryActivity) { list ->
                adapter.submitList(list)
            }
        }
    }



    private fun setupAdapter(){
        adapter = StoryAdapter()
        storyBinding.rvStory.layoutManager = LinearLayoutManager(this@StoryActivity)
        adapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClick(story: ListStoryItem) {
                val detailIntent = Intent(this@StoryActivity, DetailStoryActivity::class.java)
                detailIntent.putExtra(EXTRA_DATA, story)
                startActivity(detailIntent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@StoryActivity as Activity)
                        .toBundle()
                )
            }
        })
        storyBinding.rvStory.adapter = adapter
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}