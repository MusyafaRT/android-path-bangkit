package com.example.githubuser.ui.insert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.remote.response.UserData
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.ui.ListUserAdapter


class FollowFragment : Fragment() {

    private var position: Int = 1
    private var username: String? = null
    private lateinit var binding: FragmentFollowBinding
    private val detailViewModel: MainViewModel by viewModels()


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

        if(position == 1){
            detailViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
            detailViewModel.getFollowers(username!!)
            detailViewModel.userFollower.observe(viewLifecycleOwner) {follow ->
                setUserFollow(follow)
            }

        } else {
            detailViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
            detailViewModel.getFollowing(username!!)
            detailViewModel.userFollowing.observe(viewLifecycleOwner) {follow ->
                setUserFollow(follow)
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

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }


}