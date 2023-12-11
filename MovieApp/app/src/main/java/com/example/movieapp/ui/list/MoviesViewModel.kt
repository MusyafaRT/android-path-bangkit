package com.example.movieapp.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.MoviesRepository
import com.example.movieapp.data.UiState
import com.example.movieapp.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MoviesViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<List<MovieEntity>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<MovieEntity>>>
        get() = _uiState
    private var currentQuery: String = ""

    fun fetchTopRatedMovies() {
        if (currentQuery.isEmpty()) {
            viewModelScope.launch {
                _uiState.value = UiState.Loading
                moviesRepository.getMoviesTopRated { result ->
                    _uiState.value = result
                }
            }
        }
    }

    fun searchMovieByTitle(title: String) {
        currentQuery = title
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            moviesRepository.searchMovie(title) { result ->
                _uiState.value = result
            }
        }
    }
}