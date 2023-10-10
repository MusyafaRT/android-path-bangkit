package com.example.githubuser.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.remote.response.UserData
import com.example.githubuser.databinding.ActivityFavoriteUserBinding
import com.example.githubuser.helper.ViewModelFactory
import com.example.githubuser.ui.ListUserAdapter
import com.example.githubuser.ui.insert.DetailViewModel

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding
    private val favViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val layoutManager = LinearLayoutManager(this)
        binding.rvFavUser.layoutManager = layoutManager

        favViewModel.getFavUser().observe(this){users ->
            val favUsers = ArrayList<UserData>()
            users.map {
                val item = it.avatarUrl?.let { it1 -> UserData(login = it.username, avatarUrl = it1) }
                if (item != null) {
                    favUsers.add(item)
                }
            }
            setListUser(favUsers)
        }

    }

    private fun setListUser(userData: List<UserData>?) {
        val adapter = ListUserAdapter()
        adapter.submitList(userData)
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClick(user: UserData) {
                val detailIntent = Intent(this@FavoriteUserActivity, DetailProfileActivity::class.java)
                detailIntent.putExtra(DetailProfileActivity.EXTRA_USERNAME, user.login)
                detailIntent.putExtra(DetailProfileActivity.EXTRA_AVATAR, user.avatarUrl)
                startActivity(detailIntent)
            }
        })
        binding.rvFavUser.adapter = adapter

    }
}