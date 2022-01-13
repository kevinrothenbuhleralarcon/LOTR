package ch.kra.lotr.data.remote.dto.book

import ch.kra.lotr.data.local.entity.book.BookEntity

data class BookDto(
    val _id: String,
    val name: String
) {
    fun toBookEntity(): BookEntity {
        return BookEntity(
            id = _id,
            title = name
        )
    }
}