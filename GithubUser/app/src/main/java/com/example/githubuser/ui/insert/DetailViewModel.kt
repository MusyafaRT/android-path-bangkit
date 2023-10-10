package com.example.githubuser.ui.insert

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubuser.UserRepository
import com.example.githubuser.data.local.entity.FavoriteUser
import com.example.githubuser.data.remote.response.DetailUserResponse
import com.example.githubuser.data.remote.retrofit.ApiConfig
import com.example.githubuser.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository = UserRepository(application)
    val _detailUser = MutableLiveData<DetailUserResponse?>()
    var detailUser: LiveData<DetailUserResponse?> = _detailUser
    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun setDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {

            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val detailUserResponse = response.body()
                    if (detailUserResponse != null) {
                        _detailUser.postValue(detailUserResponse)

                    } else {
                        Log.e("DetailViewModel", "API response body is null")
                    }
                } else {
                    Log.e("DetailViewModel", "API request failed with code ${response.code()}")
                    val errorBody = response.errorBody()?.string()
                    Log.e("DetailViewModel", "Error body: $errorBody")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("MainViewModel", "onFailure: ${t.message.toString()}")
            }

        })
    }
    fun getFavUser() = userRepository.getFavUser()
    fun setFav(isFavorite: Boolean){
        _isFavorite.value = isFavorite
    }
    fun checkUser(username: String?) = userRepository.checkUser(username)
    fun removeFav(username: String?) = userRepository.removeFromFav(username)
    fun addFav(username: String?, avatar: String?){
        userRepository.addToFav(username, avatar)
    }



}