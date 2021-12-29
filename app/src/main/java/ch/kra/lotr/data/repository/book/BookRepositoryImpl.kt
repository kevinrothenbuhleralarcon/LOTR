package ch.kra.lotr.data.repository.book

import ch.kra.lotr.core.Resource
import ch.kra.lotr.data.remote.dto.LotrApi
import ch.kra.lotr.domain.model.book.Book
import ch.kra.lotr.domain.model.book.Chapter
import ch.kra.lotr.domain.repository.book.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException

class BookRepositoryImpl(
    private val api: LotrApi
): BookRepository   {
    override fun getBookList(): Flow<Resource<List<Book>>> = flow {
        emit(Resource.Loading())

        try {
            val result = api.getBookList()
            emit(Resource.Success(result.docs.map { it.toBook() }))

        } catch (e: HttpException){
            emit(Resource.Error(
                message = "Loading error"
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = "Couldn't reach server, check your internet connection."
            ))
        }
    }

    override fun getChapterList(bookId: String): Flow<Resource<List<Chapter>>> = flow {
        emit(Resource.Loading())

        try {
            val result = api.getBookChapters(bookId)
            emit(Resource.Success(result.docs.map { it.toChapter() }))

        } catch (e: HttpException){
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