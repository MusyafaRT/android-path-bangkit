package com.example.tanahku.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tanahku.data.pref.UserModel
import com.example.tanahku.data.pref.UserPreference
import com.example.tanahku.data.remote.ApiConfig
import com.example.tanahku.data.remote.ApiService
import com.example.tanahku.data.remote.LoginResponse
import com.example.tanahku.data.remote.RegisterResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TanahkuRepository(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var testToken: String? = null
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun login() {
        userPreference.login()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun registerUser(username: String, email: String, pass: String) {
        val token = testToken ?: runBlocking { userPreference.getSession().first().token }
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).register(username, email, pass)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _registerResponse.value = response.body()
                } else {
                    Log.e(
                        TAG,
                        "onFailure: ${response.message()}, ${response.body()?.data?.error}"
                    )
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("onFailure: ", t.message.toString())
            }

        })

    }

    fun loginUser(email: String, pass: String) {
        val token = testToken ?: runBlocking { userPreference.getSession().first().token }
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).loginUser(email, pass)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _loginResponse.value = response.body()
                    _loginResponse.postValue(response.body())
                } else {
                    Log.e(
                        TAG,
                        "onFailure: ${response.message()}, ${response.body()?.meta?.message}"
                    )
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(
                    TAG,
                    "onFailure: ${t.message.toString()}"
                )
            }

        })
    }

    companion object {

        const val TAG = "TanahkuRepository"

        @Volatile
        private var instance: TanahkuRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): TanahkuRepository =
            instance ?: synchronized(this) {
                instance ?: TanahkuRepository(userPreference, apiService)
            }.also { instance = it }
    }

}