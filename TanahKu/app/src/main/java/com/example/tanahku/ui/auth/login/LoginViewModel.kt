package com.example.tanahku.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.tanahku.data.TanahkuRepository
import com.example.tanahku.data.pref.UserModel
import com.example.tanahku.data.remote.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel( private val repository: TanahkuRepository) : ViewModel() {
    val loginResponse: LiveData<LoginResponse> = repository.loginResponse
    val isLoading: LiveData<Boolean> = repository.isLoading
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun loginUser(email: String, pass: String) {
        viewModelScope.launch {
            repository.loginUser(email, pass)
        }
    }

    fun login() {
        viewModelScope.launch {
            repository.login()
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

}