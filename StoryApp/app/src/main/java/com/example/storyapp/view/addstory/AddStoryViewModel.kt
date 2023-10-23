package com.example.storyapp.view.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.api.ErrorResponse
import com.example.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val userRepo: UserRepository): ViewModel() {
    val uploadResponse: LiveData<ErrorResponse> = userRepo.uploadStory
    fun uploadStory(token: String, file: MultipartBody.Part, desc: RequestBody){
        viewModelScope.launch {
            userRepo.uploadStory(token, file, desc)
        }
    }
    fun getSession(): LiveData<UserModel> {
        return userRepo.getSession().asLiveData()
    }

}