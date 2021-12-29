package ch.kra.lotr.domain.use_case

import ch.kra.lotr.core.Resource
import ch.kra.lotr.domain.model.book.Book
import ch.kra.lotr.domain.repository.book.BookRepository
import kotlinx.coroutines.flow.Flow

class GetBookList(
    private val repository: BookRepository
) {
    operator fun invoke(): Flow<Resource<List<Book>>> {
        return repository.getBookList()
    }
}