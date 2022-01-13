package ch.kra.lotr.presentation.movie.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.kra.lotr.R
import ch.kra.lotr.core.UIEvent
import ch.kra.lotr.domain.model.movie.Movie
import ch.kra.lotr.presentation.LoadingWrapper
import ch.kra.lotr.presentation.NavigateBackHeader
import ch.kra.lotr.presentation.movie.MovieDetailEvent
import ch.kra.lotr.presentation.movie.viewmodel.MovieViewModel
import kotlinx.coroutines.flow.collect

@Composable
fun MovieDetailScreen(
    viewModel: MovieViewModel,
    navigateBack: () -> Unit
) {
    val movieDetail = viewModel.movie

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.PopBackStack -> {
                    navigateBack()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
    ) {

        NavigateBackHeader(title = movieDetail?.name ?: "") {
            viewModel.onEvent(MovieDetailEvent.OnNavigateBack)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LoadingWrapper(isLoading = false) {
                movieDetail?.let { movie ->
                    MovieDetail(movie = movie)
                }
            }
        }
    }
}

@Composable
fun MovieDetail(
    movie: Movie
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.runtime) + movie.runtimeInMinutes + " min",
            color = MaterialTheme.colors.onSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text =
            stringResource(R.string.budget) +
                    movie.budgetInMillions
                    + " " +
                    stringResource(R.string.millions),
            color = MaterialTheme.colors.onSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text =
            stringResource(R.string.revenue) +
                    movie.boxOfficeRevenueInMillions
                    + " " +
                    stringResource(R.string.millions),
            color = MaterialTheme.colors.onSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text =
            stringResource(R.string.award_nomination) +
                    movie.academyAwardNominations,
            color = MaterialTheme.colors.onSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text =
            stringResource(R.string.award_won) +
                    movie.academyAwardWins,
            color = MaterialTheme.colors.onSecondary
        )
    }
}