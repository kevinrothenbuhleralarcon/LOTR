package ch.kra.lotr.presentation.book.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.kra.lotr.core.ListState
import ch.kra.lotr.core.Resource
import ch.kra.lotr.core.Routes
import ch.kra.lotr.core.UIEvent
import ch.kra.lotr.domain.model.book.Book
import ch.kra.lotr.domain.use_case.book.GetBookList
import ch.kra.lotr.presentation.book.BookListEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val getBookList: GetBookList,
) : ViewModel() {

    private val _bookListState = mutableStateOf(ListState<Book>())
    val bookListState: State<ListState<Book>> get() = _bookListState

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getBooks()
    }

    fun onEvent(event: BookListEvent) {
        when(event) {
            is BookListEvent.DisplayChapter -> {
                sendUIEvent(
                    UIEvent.Navigate(
                        Routes.CHAPTER_LIST +
                                "/${event.book.id}" +
                                "/${event.book.title}"
                    ))
            }
        }
    }

    private fun sendUIEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun getBooks() {
        viewModelScope.launch {
            getBookList()
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _bookListState.value = _bookListState.value.copy(
                                list = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }

                        is Resource.Error -> {
                            _bookListState.value = _bookListState.value.copy(
                                list = result.data ?: emptyList(),
                                isLoading = false
                            )
                            sendUIEvent(UIEvent.ShowSnackbar(
                                result.message ?: "Unknown Error"
                            ))
                        }

                        is Resource.Loading -> {
                            _bookListState.value = _bookListState.value.copy(
                                list = result.data ?: emptyList(),
                                isLoading = true
                            )
                        }
                    }
                }.launchIn(this)
        }
    }
}