package com.example.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.data.api.ApiConfig
import com.example.storyapp.data.api.ApiService
import com.example.storyapp.data.api.ErrorResponse
import com.example.storyapp.data.api.ListStoryItem
import com.example.storyapp.data.api.LoginResponse
import com.example.storyapp.data.api.StoryResponse
import com.example.storyapp.data.database.StoryDatabase
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
    private val userPreference: UserPreference,
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) {

    private val _registerResponse = MutableLiveData<ErrorResponse>()
    val registerResponse: LiveData<ErrorResponse> = _registerResponse

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _listStories = MutableLiveData<StoryResponse>()
    val listStory: LiveData<StoryResponse> = _listStories

    private val _uploadStory = MutableLiveData<ErrorResponse>()
    val uploadStory: LiveData<ErrorResponse> = _uploadStory

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

    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    fun getListStoriesWithLocation() {
        val token = testToken ?: runBlocking { userPreference.getSession().first().token }
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).getListStoriesWithLocation()
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _listStories.value = response.body()
                } else {

                    Log.e("onFailure: ", response.body()?.message.toString())
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e("onFailure: ", t.message.toString())
                _isLoading.value = false
            }

        })
    }

    fun registerUser(username: String, email: String, pass: String) {
        val token = testToken ?: runBlocking { userPreference.getSession().first().token }
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).register(username, email, pass)
        client.enqueue(object : Callback<ErrorResponse> {
            override fun onResponse(
                call: Call<ErrorResponse>,
                response: Response<ErrorResponse>
            ) {
                _isLoading.value = false
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
                        "onFailure: ${response.message()}, ${response.body()?.message.toString()}"
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

    fun uploadStory(token: String, file: MultipartBody.Part, description: RequestBody, lat: RequestBody?, lon: RequestBody?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).uploadStory(file, description, lat, lon)
        client.enqueue(object : Callback<ErrorResponse> {
            override fun onResponse(
                call: Call<ErrorResponse>,
                response: Response<ErrorResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _uploadStory.value = response.body()
                } else {
                    Log.e(
                        TAG,
                        "onFailure: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<ErrorResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("onFailure: ", t.message.toString())
            }

        })

    }

    companion object {

        const val TAG = "UserRepository"

        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            storyDatabase: StoryDatabase,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, storyDatabase, apiService)
            }.also { instance = it }
    }
}