package ch.kra.lotr.data.remote.dto.book

import ch.kra.lotr.domain.model.book.Book

data class BookDto(
    val _id: String,
    val name: String
) {
    fun toBook(): Book {
        return Book(
            id = _id,
            title = name
        )
    }
}