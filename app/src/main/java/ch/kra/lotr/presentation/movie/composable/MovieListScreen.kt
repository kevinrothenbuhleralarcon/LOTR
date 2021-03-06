package ch.kra.lotr.presentation.movie.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.kra.lotr.R
import ch.kra.lotr.core.UIEvent
import ch.kra.lotr.domain.model.movie.Movie
import ch.kra.lotr.presentation.LoadingWrapper
import ch.kra.lotr.presentation.LogoHeader
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
            LogoHeader(
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
                LoadingWrapper(isLoading = movieListState.isLoading) {
                    MovieList(
                        movieList = movieListState.list,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        }

    }
}

@Composable
private fun MovieList(
    movieList: List<Movie>,
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
            items(movieList.size) { i ->
                //the 2 first entry of the list are the info for the LOTR series and Hobbit series so we display them before the list of the respecting movies

                if (i == 2) {
                    Text(
                        text = movieList[1].name,
                        color = MaterialTheme.colors.onSecondary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onEvent(MovieListEvent.DisplayMovieDetail(movieList[1]))
                            }
                    )
                }

                if (i == 5) {
                    Text(
                        text = movieList[0].name,
                        color = MaterialTheme.colors.onSecondary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onEvent(MovieListEvent.DisplayMovieDetail(movieList[0]))
                            }
                    )
                }

                if (i != 0 && i != 1) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = movieList[i].name,
                        color = MaterialTheme.colors.onSecondary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(16.dp, 0.dp)
                            .clickable {
                                onEvent(MovieListEvent.DisplayMovieDetail(movieList[i]))
                            }
                    )
                }

                if (i == 4) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(
                        color = MaterialTheme.colors.onSecondary,
                        thickness = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}