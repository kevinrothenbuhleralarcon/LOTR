package ch.kra.lotr.data.repository.movie

import ch.kra.lotr.core.Resource
import ch.kra.lotr.data.remote.dto.LotrApi
import ch.kra.lotr.domain.model.movie.Movie
import ch.kra.lotr.domain.repository.movie.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class MovieRepositoryImpl(
    private val api: LotrApi
): MovieRepository {

    override fun getMovieList(): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading())

        try {
            val result = api.getMovieList()
            emit(Resource.Success(result.docs.map { it.toMovie() }))


        } catch (e: HttpException) {
            emit(Resource.Error(
                message = "Loading error"
            ))

        } catch (e: IOException) {
            emit(Resource.Error(
                message = "Couldn't reach server, check your internet connection."
            ))
        }
    }
}