package ch.kra.lotr.data.local.entity.book

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ch.kra.lotr.domain.model.book.Chapter

@Entity
data class ChapterEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name= "book_id") val bookId: String,
    @ColumnInfo(name = "chapter_name") val chapterName: String
) {
    fun toChapter(): Chapter {
        return Chapter(
            id = id,
            chapterName = chapterName
        )
    }
}
