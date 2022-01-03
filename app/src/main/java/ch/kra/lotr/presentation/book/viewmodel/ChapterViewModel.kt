package ch.kra.lotr.presentation.book.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.kra.lotr.core.Resource
import ch.kra.lotr.core.UIEvent
import ch.kra.lotr.domain.use_case.book.GetChapterList
import ch.kra.lotr.presentation.book.ChapterListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChapterViewModel @Inject constructor(
    private val getChapterList: GetChapterList,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var hasApiCallAlreadySucceed = false

    private val _chapterListState = mutableStateOf(ChapterListState())
    val chapterListState: State<ChapterListState> get() = _chapterListState

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow: SharedFlow<UIEvent> = _eventFlow.asSharedFlow()

    init {
        println("Book id state: ${savedStateHandle.get<String>("bookId")}")
        println("Book name state: ${savedStateHandle.get<String>("bookName")}")
    }


    fun getBookChapter(bookId: String) {
        if (!hasApiCallAlreadySucceed) {
            viewModelScope.launch {
                getChapterList(bookId)
                    .onEach { result ->
                        when (result) {
                            is Resource.Success -> {
                                _chapterListState.value = _chapterListState.value.copy(
                                    chapterList = result.data ?: emptyList(),
                                    isLoading = false
                                )
                                hasApiCallAlreadySucceed = true
                            }

                            is Resource.Error -> {
                                _chapterListState.value = _chapterListState.value.copy(
                                    chapterList = result.data ?: emptyList(),
                                    isLoading = false
                                )
                                _eventFlow.emit(UIEvent.ShowSnackbar(
                                    message = result.message ?: "Unknown error"
                                ))
                                hasApiCallAlreadySucceed = false
                            }

                            is Resource.Loading -> {
                                _chapterListState.value = _chapterListState.value.copy(
                                    chapterList = result.data ?: emptyList(),
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