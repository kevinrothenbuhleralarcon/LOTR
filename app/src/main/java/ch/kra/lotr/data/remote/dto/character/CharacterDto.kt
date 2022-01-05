package ch.kra.lotr.data.remote.dto.character

import ch.kra.lotr.domain.model.character.LotrCharacter
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory


data class CharacterDto(
    val _id: String,
    val birth: String?,
    val death: String?,
    val gender: String?,
    val hair: String?,
    val height: String?,
    val name: String,
    val race: String?,
    val realm: String?,
    val spouse: String?,
    val wikiUrl: String?
) {
    fun mapToCharacter(): LotrCharacter {
        return LotrCharacter(
            id = _id,
            birth = birth ?: "",
            death = death ?: "",
            gender = gender ?: "",
            hair = hair ?: "",
            height = height ?: "",
            name = name,
            race = race ?: "",
            realm = realm ?: "",
            spouse = spouse ?: "",
            wikiUrl = wikiUrl ?: ""
        )
    }
}