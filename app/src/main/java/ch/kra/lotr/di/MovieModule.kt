package ch.kra.lotr.di

import ch.kra.lotr.data.remote.dto.LotrApi
import ch.kra.lotr.data.repository.movie.MovieRepositoryImpl
import ch.kra.lotr.domain.repository.movie.MovieRepository
import ch.kra.lotr.domain.use_case.movie.GetMovieList
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieModule {

    @Provides
    @Singleton
    fun provideGetMovieList(repository: MovieRepository): GetMovieList {
        return GetMovieList(
            repository
        )
    }

    @Provides
    @Singleton
    fun provideMovieRepository(api: LotrApi): MovieRepository {
        return MovieRepositoryImpl(
            api
        )
    }
}