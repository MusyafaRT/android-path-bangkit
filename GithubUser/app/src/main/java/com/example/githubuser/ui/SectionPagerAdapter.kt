package com.example.githubuser.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }
    var username: String = ""
    override fun createFragment(position: Int): Fragment {
        Log.d("SectionPagerAdapter", "Creating fragment at position $position")
        val fragment = FollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowFragment.ARG_POSITION, position+1)
            putString(FollowFragment.ARG_USERNAME, username)
        }
        return fragment
    }

}