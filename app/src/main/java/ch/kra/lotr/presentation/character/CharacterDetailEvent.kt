package ch.kra.lotr.presentation.character

sealed class CharacterDetailEvent {
    object OnNavigateBack: CharacterDetailEvent()
    data class OnNavigateToWiki(val url: String): CharacterDetailEvent()
}