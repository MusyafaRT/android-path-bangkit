package com.example.storyapp.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.api.ListStoryItem
import com.example.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class StoryViewModel(
    private val repository: UserRepository
) : ViewModel() {

    val isLoading: LiveData<Boolean> = repository.isLoading
    val story: LiveData<PagingData<ListStoryItem>> = repository.getStory().cachedIn(viewModelScope)

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun login() {
        viewModelScope.launch {
            repository.login()
        }
    }

    fun logOut() {
        viewModelScope.launch {
            repository.logout()
        }
    }


}