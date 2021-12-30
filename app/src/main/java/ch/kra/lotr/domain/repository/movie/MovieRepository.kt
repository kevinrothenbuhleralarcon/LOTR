package ch.kra.lotr.domain.repository.movie

import ch.kra.lotr.core.Resource
import ch.kra.lotr.domain.model.movie.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovieList(): Flow<Resource<List<Movie>>>
}