package ch.kra.lotr.data.remote.dto.book

data class BookListDto(
    val docs: List<BookDto>,
    val limit: Int,
    val offset: Int,
    val page: Int,
    val pages: Int,
    val total: Int
)