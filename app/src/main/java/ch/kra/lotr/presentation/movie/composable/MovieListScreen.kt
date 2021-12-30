package ch.kra.lotr.presentation.movie.composable

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.kra.lotr.core.UIEvent
import ch.kra.lotr.presentation.movie.viewmodel.MovieViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MovieListScreen(
    viewModel: MovieViewModel,
    navigate: (String) -> Unit
) {
    val movieListState = viewModel.movieListState.value
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        scaffoldState = scaffoldState
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(movieListState.list.size) { i ->
                if (i > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Text(text = movieListState.list[i].name)
            }
        }
    }
}