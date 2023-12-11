package com.example.movieapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.data.local.entity.MovieEntity
import com.example.movieapp.data.local.room.MoviesDao
import com.example.movieapp.data.remote.response.DetailMoviesResponse
import com.example.movieapp.data.remote.response.MoviesResponse
import com.example.movieapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesRepository(
    private val apiService: ApiService,
    private val moviesDao: MoviesDao
) {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    fun getMoviesTopRated(callback: (UiState<List<MovieEntity>>) -> Unit) {
        _isLoading.value = true
        val client = apiService.getMovies()
        client.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    val movies = response.body()?.results?.map { result ->
                        MovieEntity(
                            id = result.id.toLong(),
                            title = result.title,
                            urlToImage = "https://image.tmdb.org/t/p/original/${result.posterPath}",
                            releasedAt = result.releaseDate
                        )
                    } ?: emptyList()
                    callback(UiState.Success(movies))
                } else {
                    Log.e("onFailure: ", "fetch data failed")
                    callback(UiState.Error("Failed to fetch data"))
                }
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("onFailure: ", "fetch data failed", t)
                callback(UiState.Error("Failed to fetch data"))
            }
        })
    }

    fun getMoviesDetail(id: Long, callback: (UiState<DetailMoviesResponse>) -> Unit) {
        _isLoading.value = true
        val client = apiService.getDetailMovie(id)
        client.enqueue(object : Callback<DetailMoviesResponse> {
            override fun onResponse(call: Call<DetailMoviesResponse>, response: Response<DetailMoviesResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {

                    callback(UiState.Success(response.body()!!))
                } else {
                    Log.e("onFailure: ", "fetch data failed")
                    callback(UiState.Error("Failed to fetch data"))
                }
            }
            override fun onFailure(call: Call<DetailMoviesResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("onFailure: ", "fetch data failed", t)
                callback(UiState.Error("Failed to fetch data"))
            }
        })
    }

    fun searchMovie(title: String, callback: (UiState<List<MovieEntity>>) -> Unit) {
        _isLoading.value = true
        val client = apiService.searchMovie(title)
        client.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    val movies = response.body()?.results?.map { result ->
                        MovieEntity(
                            id = result.id.toLong(),
                            title = result.title,
                            urlToImage = "https://image.tmdb.org/t/p/original/${result.posterPath}",
                            releasedAt = result.releaseDate
                        )
                    } ?: emptyList()
                    callback(UiState.Success(movies))
                } else {
                    Log.e("onFailure: ", "fetch data failed")
                    callback(UiState.Error("Failed to fetch data"))
                }
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("onFailure: ", "fetch data failed", t)
                callback(UiState.Error("Failed to fetch data"))
            }
        })
    }

    suspend fun getFavoriteMovies(): List<MovieEntity> {
        return withContext(Dispatchers.IO) {
            try {
                // Observe the LiveData and convert it to a regular list
                val liveDataResult = moviesDao.getFavoritedMovies()
                liveDataResult.value ?: emptyList()
            } catch (e: Exception) {
                // Handle the error if necessary
                throw e
            }
        }
    }


    suspend fun saveMovies(movies: MovieEntity) {
        moviesDao.saveMovies(movies)
    }

    suspend fun deleteMovies(title: String) {
        moviesDao.deleteMovies(title)
    }

    fun isMoviesFavorite(title: String): LiveData<Boolean> {
        return moviesDao.isMovieFavorite(title)
    }

    companion object {
        @Volatile
        private var instance: MoviesRepository? = null
        fun getInstance(
            apiService: ApiService,
            moviesDao: MoviesDao
        ): MoviesRepository =
            instance ?: synchronized(this) {
                instance ?: MoviesRepository(apiService, moviesDao)
            }.also { instance = it }
    }
}