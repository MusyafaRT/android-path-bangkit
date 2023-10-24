package com.example.storyapp.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.api.ErrorResponse
import kotlinx.coroutines.launch


class SignupViewModel(private val repository: UserRepository) : ViewModel() {
    val isLoading: LiveData<Boolean> = repository.isLoading
    val registerResponse: LiveData<ErrorResponse> = repository.registerResponse
    fun postUser(username: String, email: String, pass: String){
        viewModelScope.launch {
            repository.registerUser(username, email, pass)
        }
    }

}