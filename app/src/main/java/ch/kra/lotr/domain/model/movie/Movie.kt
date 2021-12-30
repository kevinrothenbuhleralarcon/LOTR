package ch.kra.lotr.domain.model.movie

data class Movie(
    val id: String,
    val academyAwardNominations: Int,
    val academyAwardWins: Int,
    val boxOfficeRevenueInMillions: Double,
    val budgetInMillions: Double,
    val name: String,
    val runtimeInMinutes: Double
)