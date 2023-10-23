package com.example.storyapp.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.api.ListStoryItem
import com.example.storyapp.data.pref.UserModel
import com.example.storyapp.data.pref.UserPreference
import com.example.storyapp.data.pref.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class StoryViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    val listStories: LiveData<List<ListStoryItem>> = userRepository.listStory

    init {
        getAllList(userRepository.isLogin.token)
    }

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun logOut(){
        viewModelScope.launch{
            userRepository.logout()
        }
    }

    fun getAllList(token: String) = userRepository.getListStories(token)

}