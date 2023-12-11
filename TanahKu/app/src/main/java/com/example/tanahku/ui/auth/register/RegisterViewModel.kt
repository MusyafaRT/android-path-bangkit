package com.example.tanahku.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanahku.data.TanahkuRepository
import com.example.tanahku.data.remote.RegisterResponse
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: TanahkuRepository
) : ViewModel() {
    val isLoading: LiveData<Boolean> = repository.isLoading
    val registerResponse: LiveData<RegisterResponse> = repository.registerResponse
    fun postUser(username: String, email: String, pass: String) {
        viewModelScope.launch {
            repository.registerUser(username, email, pass)
        }
    }
}