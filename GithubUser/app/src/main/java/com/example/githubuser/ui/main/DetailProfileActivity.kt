package com.example.githubuser.ui.main

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.remote.response.DetailUserResponse
import com.example.githubuser.databinding.ActivityDetailPofileBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.example.githubuser.helper.ViewModelFactory
import com.example.githubuser.ui.insert.DetailViewModel
import com.example.githubuser.ui.insert.SectionPagerAdapter
import com.example.githubuser.utils.AppExecutors


class DetailProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPofileBinding
    private val sectionPagerAdapter = SectionPagerAdapter(this)
    private val detailViewModel by viewModels<DetailViewModel>() {
        ViewModelFactory.getInstance(application)
    }
    private var appExecutors: AppExecutors = AppExecutors()

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
        )
        const val EXTRA_USERNAME = "username"
        const val EXTRA_AVATAR = "avatar"
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityDetailPofileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val avatar = intent.getStringExtra(EXTRA_AVATAR)

        var onFavClick = false

        if (username != null) {
            detailViewModel.setDetailUser(username)
            sectionPagerAdapter.username = username
        }

        detailViewModel.detailUser.observe(this) { user ->
            setDetailUser(user)
        }

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        Log.d("FavState", "status fav icon: $onFavClick")
        val fabFav = binding.fabFavorite
        appExecutors.diskIO.execute {
            val count = detailViewModel.checkUser(username)
            if(count != null){
                if(count > 0){
                    fabFav.setImageDrawable(
                        ContextCompat.getDrawable(
                            fabFav.context,
                            R.drawable.ic_favorite
                        )
                    )
                    onFavClick = true
                } else {
                    fabFav.setImageDrawable(
                        ContextCompat.getDrawable(
                            fabFav.context,
                            R.drawable.ic_favorite_empty
                        )
                    )
                    onFavClick = false
                }
                Log.d("FavState", "status fav icon: $onFavClick")
            }
        }


        Log.d("FavState", "status fav icon: $onFavClick")

        binding.fabFavorite.setOnClickListener {
            onFavClick = !onFavClick
            Log.d("FavState", "status fav icon: $onFavClick")
            if (onFavClick) {
                fabFav.setImageDrawable(
                    ContextCompat.getDrawable(
                        fabFav.context,
                        R.drawable.ic_favorite
                    )
                )
                detailViewModel.addFav(username, avatar)
            } else {
                fabFav.setImageDrawable(
                    ContextCompat.getDrawable(
                        fabFav.context,
                        R.drawable.ic_favorite_empty
                    )
                )
                detailViewModel.removeFav(username)
            }
//            Log.d("FavUser", "username: $username avatar: $avatar")
        }
    }

    private fun setDetailUser(detailUser: DetailUserResponse?) {
        binding.tvUsername.text = detailUser?.name
        binding.tvName.text = detailUser?.login
        Glide.with(this)
            .load(detailUser?.avatarUrl)
            .into(binding.imgAvatar)
        if (detailUser == null) {
            binding.tvUserFollowers.text = ""
            binding.tvUserFollowing.text = ""
        } else {
            binding.tvUserFollowing.text =
                resources.getString(R.string.following, detailUser.following)
            binding.tvUserFollowers.text =
                resources.getString(R.string.follower, detailUser.followers)
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