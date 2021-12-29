package ch.kra.lotr.data.remote.dto.book

import ch.kra.lotr.domain.model.book.Chapter

data class ChapterDto(
    val _id: String,
    val chapterName: String
) {
    fun toChapter(): Chapter {
        return Chapter(
            id = _id,
            chapterName = chapterName
        )
    }
}