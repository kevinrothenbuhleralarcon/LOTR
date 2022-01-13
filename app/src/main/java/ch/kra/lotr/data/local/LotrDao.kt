package ch.kra.lotr.data.local

import androidx.room.*
import ch.kra.lotr.data.local.entity.book.BookEntity
import ch.kra.lotr.data.local.entity.book.ChapterEntity
import ch.kra.lotr.data.local.entity.character.CharacterEntity
import ch.kra.lotr.data.local.entity.movie.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LotrDao {
    @Query("SELECT * FROM bookentity")
    suspend fun getAllBooks(): List<BookEntity>

    @Query("SELECT * FROM chapterentity WHERE book_id = :bookId")
    suspend fun getAllChapterFromBook(bookId: String): List<ChapterEntity>

    @Query("SELECT * FROM movieentity")
    suspend fun getAllMovies(): List<MovieEntity>

    @Query("SELECT * FROM characterentity")
    suspend fun getAllCharacters(): List<CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookList(bookList: List<BookEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapterList(chapterList: List<ChapterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieList(movieList: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacterList(characterList: List<CharacterEntity>)

    @Delete
    suspend fun deleteBookList(bookList: List<BookEntity>)

    @Query("DELETE FROM chapterentity WHERE book_id = :bookId")
    suspend fun deleteChapterList(bookId: String)

    @Delete
    suspend fun deleteMovieList(movieList: List<MovieEntity>)

    @Delete
    suspend fun deleteCharacterList(characterList: List<CharacterEntity>)
}