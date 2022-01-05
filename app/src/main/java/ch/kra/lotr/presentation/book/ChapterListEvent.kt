package ch.kra.lotr.presentation.book

sealed class ChapterListEvent {
    object OnNavigateBackPressed: ChapterListEvent()
}
