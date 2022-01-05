package ch.kra.lotr.data.remote.dto.character

data class CharacterListDto(
    val docs: List<CharacterDto>,
    val limit: Int,
    val offset: Int,
    val page: Int,
    val pages: Int,
    val total: Int
)