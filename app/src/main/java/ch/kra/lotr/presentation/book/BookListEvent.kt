package ch.kra.lotr.presentation.book

import ch.kra.lotr.domain.model.book.Book

sealed class BookListEvent {
    data class DisplayChapter(val book: Book): BookListEvent()
}
