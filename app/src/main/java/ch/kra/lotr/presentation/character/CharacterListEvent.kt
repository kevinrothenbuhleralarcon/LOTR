package ch.kra.lotr.presentation.character

import ch.kra.lotr.domain.model.character.LotrCharacter

sealed class CharacterListEvent {
    data class DisplayCharacterDetail(val character: LotrCharacter): CharacterListEvent()
}
