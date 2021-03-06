package ch.kra.lotr.data.remote.dto.character

import ch.kra.lotr.data.local.entity.character.CharacterEntity


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
    fun toCharacterEntity(): CharacterEntity {
        return CharacterEntity(
            id = _id,
            birth = birth,
            death = death,
            gender = gender,
            hair = hair,
            height = height,
            name = name,
            race = race,
            realm = realm,
            spouse = spouse,
            wikiUrl = wikiUrl
        )
    }
}