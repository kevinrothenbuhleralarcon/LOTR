package ch.kra.lotr.presentation.book

import ch.kra.lotr.domain.model.book.Chapter

data class ChapterListState(
    val chapterList: List<Chapter> = emptyList(),
    val isLoading: Boolean = false
)
