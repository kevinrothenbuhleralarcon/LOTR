package ch.kra.lotr.presentation.book.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.kra.lotr.core.ListState
import ch.kra.lotr.core.Resource
import ch.kra.lotr.core.UIEvent
import ch.kra.lotr.domain.model.book.Chapter
import ch.kra.lotr.domain.use_case.book.GetChapterList
import ch.kra.lotr.presentation.book.ChapterListEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChapterViewModel @Inject constructor(
    private val getChapterList: GetChapterList,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var hasApiCallAlreadySucceed = false

    private val _chapterListState = mutableStateOf(ListState<Chapter>())
    val chapterListState: State<ListState<Chapter>> get() = _chapterListState

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var bookName by mutableStateOf("")
        private set

    init {
        bookName = savedStateHandle.get<String>("bookName") ?: ""
        savedStateHandle.get<String>("bookId")?.let { bookId ->
            getBookChapter(bookId)
        }
    }

    fun onEvent(event: ChapterListEvent) {
        when (event) {
            is ChapterListEvent.OnNavigateBackPressed -> {
                sendUIEvent(UIEvent.PopBackStack)
            }
        }
    }

    private fun sendUIEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun getBookChapter(bookId: String) {
        if (!hasApiCallAlreadySucceed) {
            viewModelScope.launch {
                getChapterList(bookId)
                    .onEach { result ->
                        when (result) {
                            is Resource.Success -> {
                                _chapterListState.value = _chapterListState.value.copy(
                                    list = result.data ?: emptyList(),
                                    isLoading = false
                                )
                                hasApiCallAlreadySucceed = true
                            }

                            is Resource.Error -> {
                                _chapterListState.value = _chapterListState.value.copy(
                                    list = result.data ?: emptyList(),
                                    isLoading = false
                                )

                                sendUIEvent(
                                    UIEvent.ShowSnackbar(
                                    message = result.message ?: "Unknown error"
                                ))
                                hasApiCallAlreadySucceed = false
                            }

                            is Resource.Loading -> {
                                _chapterListState.value = _chapterListState.value.copy(
                                    list = result.data ?: emptyList(),
                                    isLoading = true
                                )
                                hasApiCallAlreadySucceed = false
                            }
                        }

                    }.launchIn(this)
            }
        }

    }
}