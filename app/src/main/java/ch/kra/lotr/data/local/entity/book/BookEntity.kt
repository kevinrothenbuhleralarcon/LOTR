package ch.kra.lotr.data.local.entity.book

import androidx.room.Entity
import androidx.room.PrimaryKey
import ch.kra.lotr.domain.model.book.Book

@Entity
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String
) {
    fun toBook(): Book {
        return Book(
            id = id,
            title = title
        )
    }
}
