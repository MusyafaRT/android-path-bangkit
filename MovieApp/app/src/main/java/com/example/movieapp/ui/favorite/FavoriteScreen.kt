package com.example.movieapp.ui.favorite

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movieapp.data.UiState
import com.example.movieapp.data.local.entity.MovieEntity
import com.example.movieapp.di.Injection
import com.example.movieapp.ui.ViewModelFactory
import com.example.movieapp.ui.list.HomeContent
import com.example.movieapp.ui.list.MoviesViewModel


@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    viewModel: FavViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
    navigateToDetail: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Log.d("FavoriteScreen", "UI State: $uiState")

    LaunchedEffect(viewModel) {
        Log.d("FavoriteScreen", "Launching getFavoriteMovies()")
        viewModel.getFavoriteMovies()
    }

    when (uiState) {
        is UiState.Loading -> {
            // Show loading UI if needed
            Log.d("FavoriteScreen", "Loading state")
        }
        is UiState.Success -> {
            val data = (uiState as UiState.Success<List<MovieEntity>>).data
            HomeContent(
                movieList = data,
                modifier = modifier,
                navigateToDetail = navigateToDetail
            )
        }
        is UiState.Error -> {
            // Handle error state if needed
            Log.e("FavoriteScreen", "Error state: $uiState")
        }
    }
}