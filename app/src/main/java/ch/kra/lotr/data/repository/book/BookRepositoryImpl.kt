package ch.kra.lotr.data.repository.book

import ch.kra.lotr.core.Resource
import ch.kra.lotr.data.local.LotrDao
import ch.kra.lotr.data.local.entity.book.ChapterEntity
import ch.kra.lotr.data.remote.dto.LotrApi
import ch.kra.lotr.domain.model.book.Book
import ch.kra.lotr.domain.model.book.Chapter
import ch.kra.lotr.domain.repository.book.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okio.IOException
import retrofit2.HttpException

class BookRepositoryImpl(
    private val dao: LotrDao,
    private val api: LotrApi
): BookRepository   {
    override fun getBookList(): Flow<Resource<List<Book>>> = flow {
        emit(Resource.Loading())

        //Retrieve the list of book we have in the cache database and sent it
        val bookList = dao.getAllBooks().map { it.toBook() }
        emit(Resource.Loading(data = bookList))

        //We try to retrieve the book list from the API, if we sucess we replace the book list that is cached and we sent it
        try {
            val newBookEntityListFromApi = api.getBookList().docs.map { it.toBookEntity() }
            dao.deleteBookList(newBookEntityListFromApi)
            dao.insertBookList(newBookEntityListFromApi)

            val newBookList = dao.getAllBooks().map { it.toBook() }
            emit(Resource.Success(data = newBookList))
        } catch (e: HttpException){
            emit(Resource.Error(
                data = bookList,
                message = "Loading error"
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                data = bookList,
                message = "Couldn't reach server, check your internet connection."
            ))
        }
    }

    override fun getChapterList(bookId: String): Flow<Resource<List<Chapter>>> = flow {
        emit(Resource.Loading())

        //Retrieve the list of chapter of a book that we have in the cache database and send it
        val chapterList = dao.getAllChapterFromBook(bookId).map { it.toChapter() }
        emit(Resource.Loading(data = chapterList))

        //We try to retrieve the chapter list for a book from the API, if we sucess we replace the book list that is cached and we sent it
        try {
            val result = api.getBookChapters(bookId)
            val newChapterEntityListFromApi = result.docs.map {
                ChapterEntity(
                    id = it._id,
                    bookId = bookId,
                    chapterName = it.chapterName
                )
            }
            dao.deleteChapterList(bookId)
            dao.insertChapterList(newChapterEntityListFromApi)

            val newChapterList = dao.getAllChapterFromBook(bookId).map { it.toChapter() }
            emit(Resource.Success(newChapterList))

        } catch (e: HttpException){
            emit(Resource.Error(
                data = chapterList,
                message = "Loading error"
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                data = chapterList,
                message = "Couldn't reach server, check your internet connection."
            ))
        }
    }
}