package com.example.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.response.DetailUserResponse
import com.example.githubuser.data.response.UserData
import com.example.githubuser.databinding.ActivityDetailPofileBinding
import com.example.githubuser.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailPofileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPofileBinding
    private val detailViewModel: MainViewModel by viewModels()
    private val sectionPagerAdapter = SectionPagerAdapter(this)

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDetailPofileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var user = intent.getStringExtra("data")

        if (user != null) {
            detailViewModel.setUser(user)
            sectionPagerAdapter.username = user
        }


        detailViewModel.detailUser.observe(this){user ->
            showLoading(false)
            setDetailUser(user)
        }

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) {tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])

        }.attach()

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        supportActionBar?.elevation = 0f
    }


    private fun setDetailUser(detailUser: DetailUserResponse?){
        binding.tvUsername.text = detailUser?.name
        binding.tvName.text = detailUser?.login
        Glide.with(this)
            .load(detailUser?.avatarUrl)
            .into(binding.imgAvatar)

        val follow = detailUser?.followers.toString()
        val following = detailUser?.following.toString()
        if(detailUser == null){
            binding.tvUserFollowers.text = ""
            binding.tvUserFollowing.text = ""
        } else{
            binding.tvUserFollowers.text = "$follow Followers"
            binding.tvUserFollowing.text = "$following Following"
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}