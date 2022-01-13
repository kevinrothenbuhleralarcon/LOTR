package ch.kra.lotr.presentation.movie.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.kra.lotr.R
import ch.kra.lotr.core.ListState
import ch.kra.lotr.core.UIEvent
import ch.kra.lotr.domain.model.movie.Movie
import ch.kra.lotr.presentation.movie.MovieListEvent
import ch.kra.lotr.presentation.movie.viewmodel.MovieViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MovieListScreen(
    viewModel: MovieViewModel,
    navigate: (UIEvent.Navigate) -> Unit
) {
    val movieListState = viewModel.movieListState.value
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is UIEvent.Navigate -> {
                    navigate(event)
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primary)
        ) {
            MovieListHeader(
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                MovieListWrapper(
                    movieListState = movieListState,
                    modifier = Modifier
                        .weight(0.8f),
                    onEvent = viewModel::onEvent
                )
            }
        }

    }
}

@Composable
private fun MovieListHeader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.lotr_header),
            contentDescription = null
        )
    }
}

@Composable
private fun MovieListWrapper(
    movieListState: ListState<Movie>,
    modifier: Modifier = Modifier,
    onEvent: (MovieListEvent) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .shadow(1.dp, RoundedCornerShape(10.dp))
            .padding(
                bottom = 8.dp,
                end = 8.dp
            )
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.secondary)
            .padding(16.dp)
    ) {
        if (movieListState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
            )
        } else {
            MovieList(
                movieListState = movieListState,
                onEvent = onEvent
            )
        }
    }
}

@Composable
private fun MovieList(
    movieListState: ListState<Movie>,
    onEvent: (MovieListEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.tolkien_movies),
            fontSize = 24.sp,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier.fillMaxWidth()
        )
        Divider(
            color = MaterialTheme.colors.onSecondary,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(movieListState.list.size) { i ->
                //the 2 first entry of the list are the info for the LOTR series and Hobbit series so we display them before the list of the respecting movies

                if (i == 2) {
                    Text(
                        text = movieListState.list[1].name,
                        color = MaterialTheme.colors.onSecondary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onEvent(MovieListEvent.DisplayMovieDetail(movieListState.list[1]))
                            }
                    )
                }

                if (i == 5) {
                    Text(
                        text = movieListState.list[0].name,
                        color = MaterialTheme.colors.onSecondary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onEvent(MovieListEvent.DisplayMovieDetail(movieListState.list[0]))
                            }
                    )
                }

                if (i != 0 && i != 1) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = movieListState.list[i].name,
                        color = MaterialTheme.colors.onSecondary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(16.dp, 0.dp)
                            .clickable {
                                onEvent(MovieListEvent.DisplayMovieDetail(movieListState.list[i]))
                            }
                    )
                }

                if (i == 4) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}