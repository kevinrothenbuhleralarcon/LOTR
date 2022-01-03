package ch.kra.lotr.presentation.book.composable


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ch.kra.lotr.R
import ch.kra.lotr.core.ListState
import ch.kra.lotr.core.UIEvent
import ch.kra.lotr.domain.model.book.Book
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
            BookListHeader(
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
                BookListWrapper(
                    bookListState = bookListState,
                    modifier = Modifier
                        .weight(0.8f),
                    onEvent = viewModel::onEvent
                )
            }
        }

    }
}

@Composable
private fun BookListHeader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.lotr_header),
            contentDescription = null
        )
    }
}

@Composable
private fun BookListWrapper(
    bookListState: ListState<Book>,
    modifier: Modifier = Modifier,
    onEvent: (BookListEvent) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .shadow(1.dp, RoundedCornerShape(10.dp))
            .padding(
                bottom = 8.dp,
                end = 8.dp
            )
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.secondary)
            .padding(16.dp)
    ) {
        if (bookListState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Center)
            )
        } else {
            BookList(
                bookListState = bookListState,
                onEvent = onEvent
            )
        }
    }
}

@Composable
private fun BookList(
    bookListState: ListState<Book>,
    onEvent: (BookListEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Lord of the rings books",
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
            items(bookListState.list.size) { i ->
                if (i > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text(
                    text = bookListState.list[i].title,
                    color = MaterialTheme.colors.onSecondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onEvent(BookListEvent.DisplayChapter(bookListState.list[i]))
                        }
                )
            }
        }
    }
}