package com.example.movieapp.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.MoviesRepository
import com.example.movieapp.data.UiState
import com.example.movieapp.data.local.entity.MovieEntity
import com.example.movieapp.data.remote.response.DetailMoviesResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailMovieViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {
    private val _detailMoviesState = MutableStateFlow<UiState<DetailMoviesResponse>>(UiState.Loading)
    val detailMoviesState: StateFlow<UiState<DetailMoviesResponse>>
        get() = _detailMoviesState

    private val movieData = MutableLiveData<MovieEntity>()

    fun setMovieData(movie: MovieEntity){
        movieData.value = movie
    }

    val favoriteStatus = movieData.switchMap {
        moviesRepository.isMoviesFavorite(it.title)
    }
    fun fetchDetailMovie(id: Long){
        moviesRepository.getMoviesDetail(id) { uiState ->
            viewModelScope.launch {
                _detailMoviesState.value = uiState
            }
        }
    }

    fun changeFavorite(movieDetail: MovieEntity) {
        viewModelScope.launch {
            val isFavorite = favoriteStatus.value ?: false
            if (isFavorite) {
                moviesRepository.deleteMovies(movieDetail.title)
            } else {
                moviesRepository.saveMovies(movieDetail)
            }
        }
    }

}