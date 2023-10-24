package com.example.storyapp.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.api.ListStoryItem
import com.example.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class StoryViewModel(
    private val repository: UserRepository
): ViewModel() {

    val listStories: LiveData<List<ListStoryItem>> = repository.listStory
    val isLoading: LiveData<Boolean> = repository.isLoading

    init {
        getAllList(repository.isLogin.token)
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun login(){
        viewModelScope.launch {
            repository.login()
        }
    }

    fun logOut(){
        viewModelScope.launch{
            repository.logout()
        }
    }

    fun getAllList(token: String) = repository.getListStories(token)

}