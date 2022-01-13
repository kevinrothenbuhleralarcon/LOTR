package ch.kra.lotr.data.remote.dto.movie

import ch.kra.lotr.data.local.entity.movie.MovieEntity

data class MovieDto(
    val _id: String,
    val academyAwardNominations: Int,
    val academyAwardWins: Int,
    val boxOfficeRevenueInMillions: Double,
    val budgetInMillions: Double,
    val name: String,
    val rottenTomatoesScore: Double,
    val runtimeInMinutes: Double
) {
    fun toMovieEntity(): MovieEntity {
        return MovieEntity(
            id = _id,
            academyAwardNominations = academyAwardNominations,
            academyAwardWins = academyAwardWins,
            boxOfficeRevenueInMillions = boxOfficeRevenueInMillions,
            budgetInMillions = budgetInMillions,
            name = name,
            runtimeInMinutes = runtimeInMinutes
        )
    }
}