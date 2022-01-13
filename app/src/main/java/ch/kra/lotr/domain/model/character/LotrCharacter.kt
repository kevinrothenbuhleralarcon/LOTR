package ch.kra.lotr.domain.model.character

data class LotrCharacter(
    val id: String,
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
)