package com.example.movieapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.data.MoviesRepository
import com.example.movieapp.di.Injection
import com.example.movieapp.ui.detail.DetailMovieViewModel
import com.example.movieapp.ui.favorite.FavViewModel
import com.example.movieapp.ui.list.MoviesViewModel

class ViewModelFactory(private val moviesRepository: MoviesRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            return MoviesViewModel(moviesRepository) as T
        } else if (modelClass.isAssignableFrom(DetailMovieViewModel::class.java)){
            return DetailMovieViewModel(moviesRepository) as T
        }else if (modelClass.isAssignableFrom(FavViewModel::class.java)){
            return FavViewModel(moviesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}