package ch.kra.lotr.presentation.character.viewModel

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.kra.lotr.core.ListState
import ch.kra.lotr.core.Resource
import ch.kra.lotr.core.Routes
import ch.kra.lotr.core.UIEvent
import ch.kra.lotr.domain.model.character.LotrCharacter
import ch.kra.lotr.domain.use_case.character.GetCharacterList
import ch.kra.lotr.presentation.character.CharacterDetailEvent
import ch.kra.lotr.presentation.character.CharacterListEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val getCharacterList: GetCharacterList
): ViewModel() {

    private val _characterList = mutableStateOf(ListState<LotrCharacter>())
    val characterList get() = _characterList

    var character by mutableStateOf<LotrCharacter?>(null)
        private set

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var searchValue by mutableStateOf("")
        private set

    private var cachedCharacterList = ListState<LotrCharacter>()
    private var isSearchStarting = true
    private var searchJob: Job? = null

    init {
        getCharacters()
    }

    fun onEvent(event: CharacterDetailEvent) {
        when (event) {
            is CharacterDetailEvent.OnNavigateBack -> {
                sendEvent(UIEvent.PopBackStack)
            }

            is CharacterDetailEvent.OnNavigateToWiki -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(event.url)
                )
                sendEvent(UIEvent.StartIntent(intent))
            }
        }
    }

    fun onEvent(event: CharacterListEvent) {
        when (event) {
            is CharacterListEvent.DisplayCharacterDetail -> {
                character = event.character
                sendEvent(UIEvent.Navigate(Routes.CHARACTER_DETAIL))
            }

            is CharacterListEvent.OnSearch -> {
                searchValue = event.searchValue
                onSearch()
            }
        }
    }

    private fun onSearch() {
        if (isSearchStarting) {
            cachedCharacterList = _characterList.value
            isSearchStarting = false
        }
        val listToSearch = cachedCharacterList.list
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (searchValue.isEmpty()) {
                _characterList.value = cachedCharacterList
                isSearchStarting = true
                return@launch
            }
            val result = listToSearch.filter {
                it.name.contains(searchValue.trim(), ignoreCase = true)
            }
            _characterList.value = _characterList.value.copy(
                list = result
            )
        }
    }

    private fun sendEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun getCharacters() {
        viewModelScope.launch {
            getCharacterList().onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _characterList.value = _characterList.value.copy(
                            list = result.data ?: listOf(),
                            isLoading = false
                        )
                    }

                    is Resource.Error -> {
                        _characterList.value = _characterList.value.copy(
                            list = result.data ?: listOf(),
                            isLoading = false
                        )

                        sendEvent(
                            UIEvent.ShowSnackbar(
                                message = result.message ?: "Unknown error"
                            )
                        )
                    }

                    is Resource.Loading -> {
                        _characterList.value = _characterList.value.copy(
                            list = result.data ?: listOf(),
                            isLoading = true
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}