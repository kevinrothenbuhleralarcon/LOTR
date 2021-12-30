package ch.kra.lotr.data.remote.dto.movie

data class MovieListDto(
    val docs: List<MovieDto>,
    val limit: Int,
    val offset: Int,
    val page: Int,
    val pages: Int,
    val total: Int
)