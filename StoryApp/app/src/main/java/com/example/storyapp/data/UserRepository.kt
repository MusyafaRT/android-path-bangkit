package com.example.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.api.ApiConfig
import com.example.storyapp.data.api.ErrorResponse
import com.example.storyapp.data.api.ListStoryItem
import com.example.storyapp.data.api.LoginResponse
import com.example.storyapp.data.api.StoryResponse
import com.example.storyapp.data.pref.UserModel
import com.example.storyapp.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun login(){
        userPreference.login()
    }
    suspend fun logout() {
        userPreference.logout()
    }

    val isLogin = runBlocking { userPreference.getSession().first() }

    private val _registerResponse = MutableLiveData<ErrorResponse>()
    val registerResponse: LiveData<ErrorResponse> = _registerResponse

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _listStories = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> = _listStories

    private val _uploadStory  = MutableLiveData<ErrorResponse>()
    val uploadStory: LiveData<ErrorResponse> = _uploadStory

    fun getListStories(token: String){
        val client = ApiConfig.getApiService(token).getStoryList()
        client.enqueue(object : Callback<StoryResponse>{
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful && response.body() != null){
                    _listStories.value = response.body()!!.listStory
                } else {
                    Log.d("onFailure: ", response.body()?.message.toString())
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.d("onFailure: ", t.message.toString())
            }

        })
    }

    fun registerUser(username: String, email: String, pass: String) {
        val client = ApiConfig.getApiService(isLogin.token).register(username, email, pass)
        client.enqueue(object : Callback<ErrorResponse> {
            override fun onResponse(
                call: Call<ErrorResponse>,
                response: Response<ErrorResponse>
            ) {
                if (response.isSuccessful) {
                    _registerResponse.value = response.body()
                } else {
                    Log.e(
                        TAG,
                        "onFailure: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<ErrorResponse>, t: Throwable) {
                Log.d("onFailure: ", t.message.toString())
            }

        })

    }

    fun loginUser(email: String, pass: String) {
        val client = ApiConfig.getApiService(isLogin.token).loginUser(email, pass)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    _loginResponse.value = response.body()
                    _loginResponse.postValue(response.body())
                } else {
                    Log.e(
                        TAG,
                        "onFailure: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(
                    TAG,
                    "onFailure: ${t.message.toString()}"
                )
            }

        })
    }

    fun uploadStory(token: String, file: MultipartBody.Part, description: RequestBody) {
        val client = ApiConfig.getApiService(token).uploadImage(file, description)
        client.enqueue(object : Callback<ErrorResponse>{
            override fun onResponse(
                call: Call<ErrorResponse>,
                response: Response<ErrorResponse>
            ) {
                if(response.isSuccessful){
                    _uploadStory.value = response.body()
                } else {
                    Log.e(
                        TAG,
                        "onFailure: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<ErrorResponse>, t: Throwable) {
                Log.d("onFailure: ", t.message.toString())
            }

        })

    }

    companion object {

        const val TAG = "UserRepository"

        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference)
            }.also { instance = it }
    }
}