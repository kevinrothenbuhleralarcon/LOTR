package ch.kra.lotr.domain.use_case.movie

import ch.kra.lotr.core.Resource
import ch.kra.lotr.domain.model.movie.Movie
import ch.kra.lotr.domain.repository.movie.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetMovieList(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<Resource<List<Movie>>> {
        return repository.getMovieList()
    }
}