package com.shmz.feature_now_playing_movies_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.shmz.core_data.model.Movie
import com.shmz.core_data.model.PlayingInfo
import com.shmz.core_ui.composnents.ErrorScreen
import com.shmz.core_ui.composnents.LoadingScreen
import com.shmz.core_utils.StartEffect
import com.shmz.core_utils.asString

@Composable
fun NowPlayingListScreen(
    navigationToMovieDetails: (Int) -> Unit,
    viewModel: NowPlayingViewModel = hiltViewModel()
) {
    StartEffect(viewModel::onStart)
    val screenState = viewModel.screenState.collectAsStateWithLifecycle().value
    NowPlayingListScreen(
        screenState = screenState,
        onPreviousPage = viewModel::onPreviousPage,
        onNextPage = viewModel::onNextPage,
        onFavoriteClick = viewModel::onFavoriteToggle,
        navigationToMovieDetails = navigationToMovieDetails
    )
}

@Composable
fun NowPlayingListScreen(
    screenState: NowPlayingListState,
    onPreviousPage: (Int) -> Unit,
    onNextPage: (Int) -> Unit,
    onFavoriteClick: (Int, Boolean) -> Unit,
    navigationToMovieDetails: (Int) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { }, // TODO
        content = {
            Box(
                modifier = Modifier
                    .padding(top = it.calculateTopPadding(), bottom = 16.dp)
                    .fillMaxWidth()
            ) {
                when (screenState) {
                    is NowPlayingListState.Error -> ErrorScreen(
                        errorMessage = screenState.errorMessage.asString(),
                        onRetry = screenState.onRetry
                    )

                    is NowPlayingListState.Idle -> MovieListWithPagination(
                        playingInfo = screenState.playingInfo,
                        onFavoriteClick = onFavoriteClick,
                        onNextPage = onNextPage,
                        onPreviousPage = onPreviousPage,
                        navigationToMovieDetails = navigationToMovieDetails
                    )

                    NowPlayingListState.Loading -> LoadingScreen()
                }
            }
        }
    )
}

@Composable
fun MovieListWithPagination(
    playingInfo: PlayingInfo,
    onFavoriteClick: (Int, Boolean) -> Unit,
    onPreviousPage: (Int) -> Unit,
    onNextPage: (Int) -> Unit,
    navigationToMovieDetails: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(playingInfo.movies) { movie ->
                MovieItem(
                    movie = movie,
                    onFavoriteClick = onFavoriteClick,
                    navigationToMovieDetails = navigationToMovieDetails
                )
            }
        }

        PaginationControls(
            currentPage = playingInfo.page,
            totalPages = playingInfo.totalPages,
            onPreviousPage = onPreviousPage,
            onNextPage = onNextPage
        )
    }
}

@Composable
fun MovieItem(
    movie: Movie,
    onFavoriteClick: (Int, Boolean) -> Unit,
    navigationToMovieDetails: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = {
            navigationToMovieDetails(movie.id)
        },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            movie.posterUrl?.let { posterUrl ->
                AsyncImage(
                    model = posterUrl,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { onFavoriteClick(movie.id, !movie.isFavorite) }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = R.string.favorite.asString(),
                            tint = if (movie.isFavorite) Color.Yellow else Color.Gray
                        )
                    }
                }

                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = R.string.release_date.asString(movie.releaseDate),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


@Composable
fun PaginationControls(
    currentPage: Int,
    totalPages: Int,
    onNextPage: (Int) -> Unit,
    onPreviousPage: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { onPreviousPage(currentPage - 1) },
            enabled = currentPage > 1
        ) {
            Text(R.string.previous.asString())
        }

        Text(
            text = R.string.page_of.asString(currentPage, totalPages),
            style = MaterialTheme.typography.bodyLarge
        )

        Button(
            onClick = { onNextPage(currentPage + 1) },
            enabled = currentPage < totalPages
        ) {
            Text(R.string.next.asString())
        }
    }
}
