package com.example.movieapp.di

import android.content.Context
import com.example.movieapp.data.MoviesRepository
import com.example.movieapp.data.local.room.MoviesDatabase
import com.example.movieapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): MoviesRepository {
        val apiService = ApiConfig.getApiService()
        val database = MoviesDatabase.getInstance(context)
        val dao = database.moviesDao()
        return MoviesRepository.getInstance(apiService, dao)
    }
}