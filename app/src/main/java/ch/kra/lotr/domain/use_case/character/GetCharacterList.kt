package ch.kra.lotr.domain.use_case.character

import ch.kra.lotr.core.Resource
import ch.kra.lotr.domain.model.character.LotrCharacter
import ch.kra.lotr.domain.repository.character.CharacterRepository
import kotlinx.coroutines.flow.Flow

class GetCharacterList(
    private val repository: CharacterRepository
) {
    operator fun invoke(): Flow<Resource<List<LotrCharacter>>> {
        return repository.getCharacterList()
    }
}