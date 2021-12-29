package ch.kra.lotr.presentation.book.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.kra.lotr.core.Resource
import ch.kra.lotr.core.UIEvent
import ch.kra.lotr.domain.repository.book.BookRepository
import ch.kra.lotr.domain.use_case.GetChapterList
import ch.kra.lotr.presentation.book.ChapterListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChapterListViewModel @Inject constructor(
    private val getChapterList: GetChapterList
): ViewModel() {

    private val _chapterListState = mutableStateOf(ChapterListState())
    val chapterListState: State<ChapterListState> get() = _chapterListState

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow: SharedFlow<UIEvent> = _eventFlow.asSharedFlow()


    fun getBookChapter(bookId: String) {
        Log.d("api", "Make Api Call")
        viewModelScope.launch {
            getChapterList(bookId)
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _chapterListState.value = _chapterListState.value.copy(
                                chapterList = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }

                        is Resource.Error -> {
                            _chapterListState.value = _chapterListState.value.copy(
                                chapterList = result.data ?: emptyList(),
                                isLoading = false
                            )
                            _eventFlow.emit(UIEvent.ShowSnackbar(
                                message = result.message ?: "Unknown error"
                            ))
                        }

                        is Resource.Loading -> {
                            _chapterListState.value = _chapterListState.value.copy(
                                chapterList = result.data ?: emptyList(),
                                isLoading = true
                            )
                        }
                    }

                }.launchIn(this)
        }
    }
}