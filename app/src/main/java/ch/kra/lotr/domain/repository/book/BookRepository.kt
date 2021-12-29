package ch.kra.lotr.domain.repository.book

import ch.kra.lotr.core.Resource
import ch.kra.lotr.domain.model.book.Book
import ch.kra.lotr.domain.model.book.Chapter
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    fun getBookList(): Flow<Resource<List<Book>>>

    fun getChapterList(bookId: String): Flow<Resource<List<Chapter>>>
}