package com.example.githubuser.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.response.UserData
import com.example.githubuser.databinding.FragmentFollowBinding


class FollowFragment : Fragment() {

    private var position: Int = 1
    private var username: String? = null
    private lateinit var binding: FragmentFollowBinding
    private val mainViewModel: MainViewModel by viewModels()

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let{
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }

        if(username != null){
            mainViewModel.setUser(username!!)
        }

        if(position == 1){
            mainViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
            mainViewModel.userFollower.observe(viewLifecycleOwner){followers ->
                if (followers != null) {
                    setUserFollow(followers)
                }
            }
        } else {
            mainViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
            mainViewModel.userFollowing.observe(viewLifecycleOwner){followers ->
                if(followers != null){
                    setUserFollow(followers)
                }
            }
        }

        binding.rvFollow.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun setUserFollow(followers: List<UserData>?){
        val adapter = ListUserAdapter()
        adapter.submitList(followers)
        binding.rvFollow.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}