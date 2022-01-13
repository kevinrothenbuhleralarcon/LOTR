package ch.kra.lotr.data.remote.dto

import ch.kra.lotr.BuildConfig
import ch.kra.lotr.data.remote.dto.book.BookListDto
import ch.kra.lotr.data.remote.dto.book.ChapterListDto
import ch.kra.lotr.data.remote.dto.character.CharacterListDto
import ch.kra.lotr.data.remote.dto.movie.MovieListDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface LotrApi {

    @GET("book")
    suspend fun getBookList(): BookListDto

    @GET("book/{id}/chapter")
    suspend fun getBookChapters(
        @Path("id") id: String
    ): ChapterListDto

    @GET("movie")
    suspend fun getMovieList(
        @Header("Authorization") authorization: String = "Bearer ${BuildConfig.TOKEN}"
    ): MovieListDto

    @GET("character?sort=name:asc")
    suspend fun getCharacterList(
        @Header("Authorization") authorization: String = "Bearer ${BuildConfig.TOKEN}"
    ): CharacterListDto
}