package com.example.githubuser.ui.insert

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.remote.response.UserData
import com.example.githubuser.data.remote.response.UserResponse
import com.example.githubuser.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class MainViewModel : ViewModel() {


    private val _listUser = MutableLiveData<List<UserData>?>()
    val listUser: LiveData<List<UserData>?> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userFollowers = MutableLiveData<List<UserData>?>()
    var userFollower: LiveData<List<UserData>?> = _userFollowers

    private val _userFollowing = MutableLiveData<List<UserData>?>()
    var userFollowing: LiveData<List<UserData>?> = _userFollowing

    private var queryUser: String = "a"

    init {
        findListUser()
    }

    fun setQueryUser(query: String){
        queryUser = query
        findListUser()
    }

    private fun findListUser() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser(queryUser)
        client.enqueue(object : Callback<UserResponse> {

            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val users = response.body()?.items
                    if (users != null) {
                        _listUser.value = users
                    } else {
                        Log.e("MainViewModel", "API response body is null")
                    }
                } else {
                    Log.e("MainViewModel", "API request failed with code ${response.code()}")
                    val errorBody = response.errorBody()?.string()
                    Log.e("MainViewModel", "Error body: $errorBody")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("MainViewModel", "onFailure: ${t.message.toString()}")
            }

        })
    }
    fun getFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<UserData>> {
            override fun onResponse(
                call: Call<List<UserData>>,
                response: Response<List<UserData>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userFollowers.value = response.body()
                } else {
                    Log.e("MainViewModel", "API request failed with code ${response.code()}")
                    val errorBody = response.errorBody()?.string()
                    Log.e("MainViewModel", "Error body: $errorBody")
                }
            }

            override fun onFailure(call: Call<List<UserData>>, t: Throwable) {
                _isLoading.value = false
                if (t is HttpException) {
                    // Handle HTTP errors
                    val statusCode = t.code()
                    val errorBody = t.response()?.errorBody()?.string()
                    Log.e("MainViewModel Follow", "API request failed with code $statusCode")
                    Log.e("MainViewModel", "Error body: $errorBody")
                } else {
                    // Handle other types of errors
                    Log.e("MainViewModel", "onFailure: ${t.message.toString()}")
                }
            }
        })
    }

    fun getFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<UserData>> {
            override fun onResponse(
                call: Call<List<UserData>>,
                response: Response<List<UserData>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userFollowing.value = response.body()
                } else {
                    Log.e("MainViewModel Follow", "API request failed with code ${response.code()}")
                    val errorBody = response.errorBody()?.string()
                    Log.e("MainViewModel", "Error body: $errorBody")
                }
            }

            override fun onFailure(call: Call<List<UserData>>, t: Throwable) {
                _isLoading.value = false
                if (t is HttpException) {
                    // Handle HTTP errors
                    val statusCode = t.code()
                    val errorBody = t.response()?.errorBody()?.string()
                    Log.e("MainViewModel Follow", "API request failed with code $statusCode")
                    Log.e("MainViewModel", "Error body: $errorBody")
                } else {
                    // Handle other types of errors
                    Log.e("MainViewModel", "onFailure: ${t.message.toString()}")
                }
            }
        })
    }
}