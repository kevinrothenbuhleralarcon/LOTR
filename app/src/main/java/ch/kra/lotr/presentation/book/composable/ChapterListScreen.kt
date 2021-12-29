package ch.kra.lotr.presentation.book.composable

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ch.kra.lotr.core.UIEvent
import ch.kra.lotr.presentation.book.viewmodel.ChapterListViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChapterListScreen(
    bookId: String,
    bookName: String,
    viewModel: ChapterListViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val chapterListState = viewModel.chapterListState.value
    val scaffoldState = rememberScaffoldState()

    Log.d("api", "test")

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.secondary)
        ) {
            Text(
                text = bookName,
                fontSize = 28.sp
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(chapterListState.chapterList.size) { i ->
                    if (i > 0) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Text(text = chapterListState.chapterList[i].chapterName)
                }
            }
        }
    }
}