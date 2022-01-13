package ch.kra.lotr.data.repository.character

import ch.kra.lotr.core.Resource
import ch.kra.lotr.data.local.LotrDao
import ch.kra.lotr.data.remote.dto.LotrApi
import ch.kra.lotr.domain.model.character.LotrCharacter
import ch.kra.lotr.domain.repository.character.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class CharacterRepositoryImpl(
    private val dao: LotrDao,
    private val api: LotrApi
): CharacterRepository {

    override fun getCharacterList(): Flow<Resource<List<LotrCharacter>>> = flow {
        emit(Resource.Loading())

        val characterList = dao.getAllCharacters().map { it.toLotrCharacter() }
        emit(Resource.Loading(data = characterList))

        try {
            val characterEntityListFromApi = api.getCharacterList().docs.map { it.toCharacterEntity() }
            dao.deleteCharacterList(characterEntityListFromApi)
            dao.insertCharacterList(characterEntityListFromApi)

            val newCharacterList = dao.getAllCharacters().map { it.toLotrCharacter() }
            emit(Resource.Success(newCharacterList))

        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    data = characterList,
                    message = "Loading error"
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    data = characterList,
                    message = "Couldn't reach server, check your internet connection."
                )
            )
        }
    }
}