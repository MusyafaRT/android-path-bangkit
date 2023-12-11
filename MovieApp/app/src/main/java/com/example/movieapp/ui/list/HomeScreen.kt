package com.example.movieapp.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movieapp.R
import com.example.movieapp.data.UiState
import com.example.movieapp.data.local.entity.MovieEntity
import com.example.movieapp.di.Injection
import com.example.movieapp.ui.ViewModelFactory
import com.example.movieapp.ui.component.MovieItem
import com.example.movieapp.ui.component.Search
import com.example.movieapp.ui.detail.DetailContent
import com.example.movieapp.ui.theme.MovieAppTheme


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MoviesViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
    navigateToDetail: (Long) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.fetchTopRatedMovies()
    }

    var searchQuery by remember { mutableStateOf("") }


    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        // Add the Search component
        Search(
            query = searchQuery,
            onQueryChange = { newQuery ->
                searchQuery = newQuery
                viewModel.searchMovieByTitle(newQuery)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )

        viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    viewModel.fetchTopRatedMovies()
                }

                is UiState.Success -> {
                    val data = uiState.data
                    HomeContent(
                        movieList = data,
                        modifier = modifier,
                        navigateToDetail = navigateToDetail
                    )
                }

                is UiState.Error -> {
                }
            }
        }
    }

}

@Composable
fun HomeContent(
    movieList: List<MovieEntity>, modifier: Modifier = Modifier, navigateToDetail: (Long) -> Unit
) {
    if (movieList.isEmpty()) {
        // Handle case when the list is empty (e.g., show a message)
        Text(text = stringResource(R.string.not_found))
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.testTag("RewardList")
        ) {
            items(movieList, key = { it.id }) { data ->
                MovieItem(
                    title = data.title,
                    image = data.urlToImage,
                    release = data.releasedAt,
                    modifier = Modifier.clickable {
                        navigateToDetail(data.id)
                    }
                )
            }
        }
    }
}
