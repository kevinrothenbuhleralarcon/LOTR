package ch.kra.lotr.presentation.book.composable


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ch.kra.lotr.R
import ch.kra.lotr.core.UIEvent
import ch.kra.lotr.domain.model.book.Book
import ch.kra.lotr.presentation.LoadingWrapper
import ch.kra.lotr.presentation.LogoHeader
import ch.kra.lotr.presentation.book.BookListEvent
import ch.kra.lotr.presentation.book.viewmodel.BookViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun BookListScreen(
    viewModel: BookViewModel = hiltViewModel(),
    navigate: (UIEvent.Navigate) -> Unit
) {
    val bookListState = viewModel.bookListState.value
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is UIEvent.Navigate -> {
                    navigate(event)
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primary)
        ) {
            LogoHeader(
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
               LoadingWrapper(isLoading = bookListState.isLoading) {
                   BookList(
                       bookList = bookListState.list,
                       onEvent = viewModel::onEvent
                   )
               }
            }
        }

    }
}

@Composable
private fun BookList(
    bookList: List<Book>,
    onEvent: (BookListEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.lotr_books),
            fontSize = 24.sp,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier.fillMaxWidth()
        )
        Divider(
            color = MaterialTheme.colors.onSecondary,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(bookList.size) { i ->
                if (i > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text(
                    text = bookList[i].title,
                    color = MaterialTheme.colors.onSecondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onEvent(BookListEvent.DisplayChapter(bookList[i]))
                        }
                )
            }
        }
    }
}