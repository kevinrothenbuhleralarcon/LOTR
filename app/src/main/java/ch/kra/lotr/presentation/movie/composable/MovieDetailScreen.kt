package ch.kra.lotr.presentation.movie.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.kra.lotr.R
import ch.kra.lotr.core.ListState
import ch.kra.lotr.core.UIEvent
import ch.kra.lotr.domain.model.book.Chapter
import ch.kra.lotr.domain.model.movie.Movie
import ch.kra.lotr.presentation.book.composable.ChapterList
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
        MovieListHeader(
            movieName = movieDetail?.name ?: "",
            onEvent = viewModel::onEvent
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(1.dp, RoundedCornerShape(10.dp))
                    .padding(
                        end = 8.dp,
                        bottom = 8.dp
                    )
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colors.secondary)
                    .padding(16.dp)
            ) {
                movieDetail?.let {
                    MovieDetail(movie = it)
                }
            }
        }
    }
}

@Composable
private fun MovieListHeader(
    movieName: String,
    modifier: Modifier = Modifier,
    onEvent: (MovieDetailEvent) -> Unit
) {
    Row(
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .size(30.dp)
                .offset(16.dp, 16.dp)
                .clickable { onEvent(MovieDetailEvent.OnNavigateBack) }
        )

        Spacer(modifier = Modifier.width(32.dp))

        Text(
            text = movieName,
            fontSize = 24.sp,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .offset((0).dp, 16.dp)
        )
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