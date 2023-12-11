package com.example.storyapp.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.api.ListStoryItem
import com.example.storyapp.data.api.StoryResponse
import com.example.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: UserRepository) : ViewModel() {

    val listStories: LiveData<StoryResponse> = repository.listStory
    val isLoading: LiveData<Boolean> = repository.isLoading
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    fun getLocationStories() {
        viewModelScope.launch {
            repository.getListStoriesWithLocation()
        }
    }
}