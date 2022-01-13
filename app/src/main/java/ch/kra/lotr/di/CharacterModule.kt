package ch.kra.lotr.di

import ch.kra.lotr.data.local.LotrDatabase
import ch.kra.lotr.data.remote.dto.LotrApi
import ch.kra.lotr.data.repository.character.CharacterRepositoryImpl
import ch.kra.lotr.domain.repository.character.CharacterRepository
import ch.kra.lotr.domain.use_case.character.GetCharacterList
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CharacterModule {

    @Provides
    @Singleton
    fun provideGetCharacterList(repository: CharacterRepository): GetCharacterList {
        return GetCharacterList(
            repository
        )
    }

    @Provides
    @Singleton
    fun provideCharacterRepository(db: LotrDatabase, api: LotrApi): CharacterRepository {
        return CharacterRepositoryImpl(
            db.lotrDao,
            api
        )
    }
}