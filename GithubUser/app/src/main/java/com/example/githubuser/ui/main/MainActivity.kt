package com.example.githubuser.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.remote.response.UserData
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.helper.SettingViewModelFactory
import com.example.githubuser.ui.ListUserAdapter
import com.example.githubuser.ui.insert.MainViewModel
import com.example.githubuser.ui.insert.SettingViewModel
import com.example.githubuser.utils.SettingPreferences


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.topAppBar.setOnMenuItemClickListener{
                menuItem -> when(menuItem.itemId) {
            R.id.menu1 -> {
                val intent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu2 -> {
                val intent = Intent(this@MainActivity, MenuActivity::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
        }

        with(binding){
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener{ textView, actionId, event ->
                    searchBar.text = searchView.text
                    mainViewModel.setQueryUser(searchView.text.toString())
                    searchView.hide()

                    false
                }
        }
        mainViewModel.listUser.observe(this){ users ->
            setListUser(users)
        }

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(pref)
        )[SettingViewModel::class.java]


        settingViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager



    }

    private fun setListUser(userData: List<UserData>?) {
        val adapter = ListUserAdapter()
        adapter.submitList(userData)
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClick(user: UserData) {
                val detailIntent = Intent(this@MainActivity, DetailProfileActivity::class.java)
                detailIntent.putExtra(DetailProfileActivity.EXTRA_USERNAME, user.login)
                detailIntent.putExtra(DetailProfileActivity.EXTRA_AVATAR, user.avatarUrl)
                startActivity(detailIntent)
            }
        })
        binding.rvReview.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}