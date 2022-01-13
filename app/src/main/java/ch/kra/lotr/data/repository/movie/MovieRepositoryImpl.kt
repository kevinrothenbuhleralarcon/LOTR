package ch.kra.lotr.data.repository.movie

import ch.kra.lotr.core.Resource
import ch.kra.lotr.data.local.LotrDao
import ch.kra.lotr.data.remote.dto.LotrApi
import ch.kra.lotr.domain.model.movie.Movie
import ch.kra.lotr.domain.repository.movie.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class MovieRepositoryImpl(
    private val dao: LotrDao,
    private val api: LotrApi
): MovieRepository {

    override fun getMovieList(): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading())

        //Retrieve the list of movie that we have in the cache database and send it
        val movieList = dao.getAllMovies().map { it.toMovie() }
        emit(Resource.Loading(movieList))

        //We try to retrieve the movie list from the API, if we sucess we replace the movie list that is cached and we sent it
        try {
            val movieEntityListFromApi = api.getMovieList().docs.map { it.toMovieEntity() }
            dao.deleteMovieList(movieEntityListFromApi)
            dao.insertMovieList(movieEntityListFromApi)

            val newMovieList = dao.getAllMovies().map { it.toMovie() }
            emit(Resource.Success(newMovieList))

        } catch (e: HttpException) {
            emit(Resource.Error(
                data = movieList,
                message = "Loading error"
            ))

        } catch (e: IOException) {
            emit(Resource.Error(
                data = movieList,
                message = "Couldn't reach server, check your internet connection."
            ))
        }
    }
}