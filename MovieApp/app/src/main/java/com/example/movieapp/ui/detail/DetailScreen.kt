package com.example.movieapp.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.movieapp.R
import com.example.movieapp.data.UiState
import com.example.movieapp.data.local.entity.MovieEntity
import com.example.movieapp.data.remote.response.GenresItem
import com.example.movieapp.di.Injection
import com.example.movieapp.ui.ViewModelFactory
import com.example.movieapp.ui.theme.MovieAppTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun DetailScreen(
    movieId: Long,
    viewModel: DetailMovieViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository(LocalContext.current)
        )
    ),
    navigateBack: () -> Unit,
//    navigateToFav: () -> Unit,
) {
//    val favoriteStatus by viewModel.favoriteStatus.observeAsState(false)
    viewModel.detailMoviesState.collectAsState(initial = UiState.Loading).value.let { uiState ->

        when (uiState) {
            is UiState.Loading -> {
                viewModel.fetchDetailMovie(movieId)
            }

            is UiState.Success -> {
                val data = uiState.data
//                viewModel.setMovieData(
//                    MovieEntity(
//                        id = data.id.toLong(),
//                        title = data.title,
//                        urlToImage = "https://image.tmdb.org/t/p/original/${data.posterPath}",
//                        releasedAt = data.releaseDate
//                    ))
                DetailContent(
                    title = data.title,
                    image = "https://image.tmdb.org/t/p/original/${data.posterPath}",
                    overview = data.overview,
                    releaseDate = data.releaseDate,
                    genres = data.genres,
                    onBackClick = navigateBack,
//                    onAddToFav = {
//                        viewModel.changeFavorite(
//                            MovieEntity(
//                                id = data.id.toLong(),
//                                title = data.title,
//                                urlToImage = "https://image.tmdb.org/t/p/original/${data.posterPath}",
//                                releasedAt = data.releaseDate
//                            )
//                        )
////                        navigateToFav()
//                    },
//                    favoriteStatus,
                )
            }

            is UiState.Error -> {
            }
        }
    }
}

@Composable
fun DetailContent(
    title: String,
    image: String,
    overview: String,
    releaseDate: String,
    genres: List<GenresItem>,
    onBackClick: () -> Unit,
//    onAddToFav: () -> Unit,
//    favoriteStatus: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            Box {
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = modifier
                        .height(400.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                )
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { onBackClick() }
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                )
                Text(
                    text = releaseDate,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = MaterialTheme.colorScheme.secondary
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = modifier.padding(bottom = 16.dp)
                ) {
                    items(genres, key = { it.id }) { genre ->
                        Text(
                            text = genre.name,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                Text(
                    text = stringResource(R.string.synopsis),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Start,
                    modifier = modifier
                        .padding(top = 8.dp)
                        .align(Alignment.Start)
                )
                Text(
                    text = overview,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                )
            }
        }
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            contentAlignment = Alignment.BottomEnd
//        ) {
//            IconButton(onClick = onAddToFav) {
//                Icon(
//                    painter = if(favoriteStatus){
//                        painterResource(R.drawable.baseline_favorite_24)
//                    } else {
//                        painterResource(R.drawable.baseline_favorite_border_24)
//                    },
//                    contentDescription = stringResource(R.string.menu_fav),
//                )
//            }
//        }
    }
}
