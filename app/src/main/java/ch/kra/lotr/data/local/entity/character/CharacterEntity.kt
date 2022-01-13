package ch.kra.lotr.data.local.entity.character

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ch.kra.lotr.domain.model.character.LotrCharacter

@Entity
data class CharacterEntity(
    @PrimaryKey val id: String,
    val birth: String?,
    val death: String?,
    val gender: String?,
    val hair: String?,
    val height: String?,
    val name: String,
    val race: String?,
    val realm: String?,
    val spouse: String?,
    @ColumnInfo(name = "wiki_url") val wikiUrl: String?
) {
    fun toLotrCharacter(): LotrCharacter {
        return LotrCharacter(
            id = id,
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
