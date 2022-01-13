package ch.kra.lotr.data.local.entity.movie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ch.kra.lotr.domain.model.movie.Movie

@Entity
data class MovieEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "academy_award_nomination") val academyAwardNominations: Int,
    @ColumnInfo(name = "academy_award_wins") val academyAwardWins: Int,
    @ColumnInfo(name = "box_office_revenue_in_millions") val boxOfficeRevenueInMillions: Double,
    @ColumnInfo(name = "budget_in_millions") val budgetInMillions: Double,
    val name: String,
    @ColumnInfo(name = "runtime_in_minutes") val runtimeInMinutes: Double
) {
    fun toMovie(): Movie {
        return Movie(
            id = id,
            academyAwardNominations = academyAwardNominations,
            academyAwardWins = academyAwardWins,
            boxOfficeRevenueInMillions = boxOfficeRevenueInMillions,
            budgetInMillions = budgetInMillions,
            name = name,
            runtimeInMinutes = runtimeInMinutes
        )
    }
}
