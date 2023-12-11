package com.example.tanahku.di

import android.content.Context
import com.example.tanahku.data.TanahkuRepository
import com.example.tanahku.data.pref.UserPreference
import com.example.tanahku.data.pref.dataStore
import com.example.tanahku.data.remote.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): TanahkuRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return TanahkuRepository.getInstance(pref, apiService)
    }
}