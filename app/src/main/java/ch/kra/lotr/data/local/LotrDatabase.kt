package ch.kra.lotr.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ch.kra.lotr.data.local.entity.book.BookEntity
import ch.kra.lotr.data.local.entity.book.ChapterEntity
import ch.kra.lotr.data.local.entity.character.CharacterEntity
import ch.kra.lotr.data.local.entity.movie.MovieEntity

@Database(
    entities = [
        BookEntity::class,
        ChapterEntity::class,
        MovieEntity::class,
        CharacterEntity::class
    ],
    version = 1
)
abstract class LotrDatabase: RoomDatabase() {
    abstract val lotrDao: LotrDao
}