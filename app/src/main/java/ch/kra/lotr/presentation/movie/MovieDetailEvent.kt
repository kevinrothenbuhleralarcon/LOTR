package ch.kra.lotr.presentation.movie

sealed class MovieDetailEvent {
    object OnNavigateBack: MovieDetailEvent()
}