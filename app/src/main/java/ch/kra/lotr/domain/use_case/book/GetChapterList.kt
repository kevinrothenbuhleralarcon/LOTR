package ch.kra.lotr.domain.use_case.book

import ch.kra.lotr.core.Resource
import ch.kra.lotr.domain.model.book.Chapter
import ch.kra.lotr.domain.repository.book.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetChapterList(
    private val repository: BookRepository
) {
    operator fun invoke(bookId: String): Flow<Resource<List<Chapter>>> {
        if (bookId.isBlank()) {
            return flow {  }
        }
        return repository.getChapterList(bookId = bookId)
    }
}