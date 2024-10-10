package com.shmz.feature_movie_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.shmz.core_data.model.Movie
import com.shmz.core_ui.composnents.ErrorScreen
import com.shmz.core_ui.composnents.LoadingScreen
import com.shmz.core_utils.StartEffect
import com.shmz.core_utils.asString

@Composable
fun MovieDetailsScreen(
    movieId: Int,
    onNavigateBack: () -> Unit,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    StartEffect {
        viewModel.onStart(movieId)
    }
    val screenState = viewModel.screenState.collectAsStateWithLifecycle().value
    MovieDetailsScreen(
        screenState = screenState,
        onFavoriteToggle = viewModel::onFavoriteToggle,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    screenState: MovieDetailsScreenState,
    onFavoriteToggle: (Int, Boolean) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    when (screenState) {
                        is MovieDetailsScreenState.Idle -> {
                            IconButton(onClick = {
                                onFavoriteToggle(
                                    screenState.movie.id,
                                    !screenState.movie.isFavorite
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (screenState.movie.isFavorite) Color.Yellow else Color.Gray
                                )
                            }
                        }

                        else -> Unit
                    }
                }
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .padding(top = it.calculateTopPadding(), bottom = 16.dp)
                    .fillMaxWidth()
            ) {
                when (screenState) {
                    is MovieDetailsScreenState.Idle -> MovieDetails(
                        screenState.movie
                    )

                    MovieDetailsScreenState.Loading -> LoadingScreen()
                    MovieDetailsScreenState.Error -> ErrorScreen()
                }
            }
        }
    )
}

@Composable
fun MovieDetails(movie: Movie) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        movie.backdropUrl?.let { posterUrl ->
            AsyncImage(
                model = posterUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.FillWidth
            )
        }

        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = R.string.rating_10.asString(movie.voteAverage),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = R.string.votes.asString(movie.voteCount),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        Text(
            text = R.string.release_date.asString(movie.releaseDate),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Text(
            text = movie.overview,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        if (movie.isAdult) {
            Text(
                text = R.string.adult_content.asString(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}