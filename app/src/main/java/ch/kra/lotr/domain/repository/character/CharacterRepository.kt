package ch.kra.lotr.domain.repository.character

import ch.kra.lotr.core.Resource
import ch.kra.lotr.domain.model.character.LotrCharacter
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    fun getCharacterList(): Flow<Resource<List<LotrCharacter>>>
}