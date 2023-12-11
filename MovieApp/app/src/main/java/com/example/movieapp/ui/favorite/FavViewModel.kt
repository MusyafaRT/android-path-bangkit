package com.example.movieapp.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.MoviesRepository
import com.example.movieapp.data.UiState
import com.example.movieapp.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<MovieEntity>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<MovieEntity>>>
        get() = _uiState

    fun getFavoriteMovies(){
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                val favoriteMovies = moviesRepository.getFavoriteMovies()
                _uiState.value = UiState.Success(favoriteMovies)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to fetch favorite movies: ${e.message}")
            }
        }
    }
}