package ch.kra.lotr.presentation.movie.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.kra.lotr.core.ListState
import ch.kra.lotr.core.Resource
import ch.kra.lotr.core.UIEvent
import ch.kra.lotr.domain.model.movie.Movie
import ch.kra.lotr.domain.use_case.movie.GetMovieList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val getMovieList: GetMovieList
): ViewModel() {

    private val _movieListState = mutableStateOf(ListState<Movie>())
    val movieListState: State<ListState<Movie>> get() = _movieListState

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getMovies()
    }

    private fun getMovies() {
        viewModelScope.launch {
            getMovieList().onEach { result ->
                when(result) {
                    is Resource.Success -> {
                        _movieListState.value = _movieListState.value.copy(
                            list = result.data ?: listOf(),
                            isLoading = false
                        )
                    }

                    is Resource.Error -> {
                        _movieListState.value = _movieListState.value.copy(
                            list = result.data ?: listOf(),
                            isLoading = false
                        )
                        _eventFlow.emit(
                            UIEvent.ShowSnackbar(
                                message = result.message ?: "Unknown Error"
                            )
                        )
                    }

                    is Resource.Loading -> {
                        _movieListState.value = _movieListState.value.copy(
                            list = result.data ?: listOf(),
                            isLoading = true
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}