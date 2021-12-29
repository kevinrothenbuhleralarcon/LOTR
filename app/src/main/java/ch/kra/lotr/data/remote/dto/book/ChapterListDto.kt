package ch.kra.lotr.data.remote.dto.book

data class ChapterListDto(
    val docs: List<ChapterDto>,
    val limit: Int,
    val offset: Int,
    val page: Int,
    val pages: Int,
    val total: Int
)