package ch.kra.lotr.data.remote.dto

import ch.kra.lotr.data.remote.dto.book.BookListDto
import ch.kra.lotr.data.remote.dto.book.ChapterListDto
import retrofit2.http.GET
import retrofit2.http.Path

interface LotrApi {

    @GET("book")
    suspend fun getBookList(): BookListDto

    @GET("book/{id}/chapter")
    suspend fun getBookChapters(
        @Path("id") id: String
    ): ChapterListDto
}