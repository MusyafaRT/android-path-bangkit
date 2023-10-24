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

class AddStoryViewModel(private val repository: UserRepository): ViewModel() {
    val uploadResponse: LiveData<ErrorResponse> = repository.uploadStory
    val isLoading: LiveData<Boolean> = repository.isLoading
    fun uploadStory(token: String, file: MultipartBody.Part, desc: RequestBody){
        viewModelScope.launch {
            repository.uploadStory(token, file, desc)
        }
    }
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

}