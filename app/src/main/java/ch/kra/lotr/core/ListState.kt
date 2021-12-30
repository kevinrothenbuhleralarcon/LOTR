package ch.kra.lotr.core

data class ListState<T>(
    val list: List<T> = listOf(),
    val isLoading: Boolean = false
)
