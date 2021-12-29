package ch.kra.lotr.presentation.book

import ch.kra.lotr.domain.model.book.Book

data class BookListState(
    val bookList: List<Book> = emptyList(),
    val isLoading: Boolean = false
)
