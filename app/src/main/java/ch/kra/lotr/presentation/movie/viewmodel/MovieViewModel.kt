package ch.kra.lotr.presentation.movie.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.kra.lotr.core.ListState
import ch.kra.lotr.core.Resource
import ch.kra.lotr.core.Routes
import ch.kra.lotr.core.UIEvent
import ch.kra.lotr.domain.model.movie.Movie
import ch.kra.lotr.domain.use_case.movie.GetMovieList
import ch.kra.lotr.presentation.movie.MovieDetailEvent
import ch.kra.lotr.presentation.movie.MovieListEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val getMovieList: GetMovieList
): ViewModel() {

    private val _movieListState = mutableStateOf(ListState<Movie>())
    val movieListState: State<ListState<Movie>> get() = _movieListState

    var movie by mutableStateOf<Movie?>(null)
        private set

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getMovies()
    }

    fun onEvent(event: MovieDetailEvent) {
        when (event) {
            is MovieDetailEvent.OnNavigateBack -> {
                sendEvent(UIEvent.PopBackStack)
            }
        }
    }

    fun onEvent(event: MovieListEvent) {
        when (event) {
            is MovieListEvent.DisplayMovieDetail -> {
                movie = event.movie
                sendEvent(UIEvent.Navigate(Routes.MOVIE_DETAIL))
            }
        }
    }

    private fun sendEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
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
                        sendEvent(
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