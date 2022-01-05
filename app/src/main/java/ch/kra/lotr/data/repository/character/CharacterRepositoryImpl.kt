package ch.kra.lotr.data.repository.character

import ch.kra.lotr.core.Resource
import ch.kra.lotr.data.remote.dto.LotrApi
import ch.kra.lotr.domain.model.character.LotrCharacter
import ch.kra.lotr.domain.repository.character.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class CharacterRepositoryImpl(
    private val api: LotrApi
): CharacterRepository {

    override fun getCharacterList(): Flow<Resource<List<LotrCharacter>>> = flow {
        emit(Resource.Loading())

        try {
            val result = api.getCharacterList()
            emit(Resource.Success(data = result.docs.map { it.mapToCharacter() }))

        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = "Loading error"
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "Couldn't reach server, check your internet connection."
                )
            )
        }
    }
}