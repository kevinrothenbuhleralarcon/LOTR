package ch.kra.lotr.presentation.book.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.kra.lotr.core.Resource
import ch.kra.lotr.core.UIEvent
import ch.kra.lotr.domain.use_case.GetBookList
import ch.kra.lotr.presentation.book.BookListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val getBookList: GetBookList,
) : ViewModel() {

    private val _bookListState = mutableStateOf(BookListState())
    val bookListState: State<BookListState> get() = _bookListState

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getBooks()
    }

    private fun getBooks() {
        viewModelScope.launch {
            getBookList()
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _bookListState.value = _bookListState.value.copy(
                                bookList = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }

                        is Resource.Error -> {
                            _bookListState.value = _bookListState.value.copy(
                                bookList = result.data ?: emptyList(),
                                isLoading = false
                            )
                            _eventFlow.emit(
                                UIEvent.ShowSnackbar(
                                    result.message ?: "Unknown Error"
                                )
                            )
                        }

                        is Resource.Loading -> {
                            _bookListState.value = _bookListState.value.copy(
                                bookList = result.data ?: emptyList(),
                                isLoading = true
                            )
                        }
                    }
                }.launchIn(this)
        }
    }
}