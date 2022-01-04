package ch.kra.lotr.presentation.movie

import ch.kra.lotr.domain.model.movie.Movie

sealed class MovieListEvent {
    data class DisplayMovieDetail(val movie: Movie): MovieListEvent()
}
