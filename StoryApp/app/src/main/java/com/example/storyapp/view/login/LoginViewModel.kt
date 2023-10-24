package com.example.storyapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.api.LoginResponse
import com.example.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch
import okhttp3.internal.userAgent

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    val loginResponse: LiveData<LoginResponse> = repository.loginResponse
    val isLoading: LiveData<Boolean> = repository.isLoading
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun loginUser(email: String, pass: String){
        viewModelScope.launch {
            repository.loginUser(email, pass)
        }
    }

    fun login(){
        viewModelScope.launch {
            repository.login()
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}