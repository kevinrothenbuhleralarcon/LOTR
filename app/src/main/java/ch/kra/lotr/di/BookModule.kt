package ch.kra.lotr.di

import ch.kra.lotr.data.local.LotrDatabase
import ch.kra.lotr.data.remote.dto.LotrApi
import ch.kra.lotr.data.repository.book.BookRepositoryImpl
import ch.kra.lotr.domain.repository.book.BookRepository
import ch.kra.lotr.domain.use_case.book.GetBookList
import ch.kra.lotr.domain.use_case.book.GetChapterList
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BookModule {

    @Provides
    @Singleton
    fun provideGetBookList(repository: BookRepository): GetBookList {
        return GetBookList(
            repository
        )
    }

    @Provides
    @Singleton
    fun provideGetChapterList(repository: BookRepository): GetChapterList {
        return GetChapterList(
            repository
        )
    }

    @Provides
    @Singleton
    fun provideBookRepository(db: LotrDatabase, api: LotrApi): BookRepository {
        return BookRepositoryImpl(
            db.lotrDao,
            api
        )
    }
}