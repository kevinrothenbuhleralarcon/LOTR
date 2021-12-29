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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ch.kra.lotr.R
import ch.kra.lotr.core.UIEvent
import ch.kra.lotr.presentation.book.viewmodel.BookViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun BookListScreen(
    viewModel: BookViewModel = hiltViewModel(),
    navigate: (String, String) -> Unit
) {
    val bookListState = viewModel.bookListState.value
    val scaffoldState = rememberScaffoldState()

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
                    .padding(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
                    .shadow(8.dp, RoundedCornerShape(8.dp))
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colors.secondary)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Lord of the rings books",
                    fontSize = 28.sp,
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
                    items(bookListState.bookList.size) { i ->
                        if (i > 0) {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        Text(
                            text = bookListState.bookList[i].title,
                            fontSize = 20.sp,
                            color = MaterialTheme.colors.onSecondary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navigate(
                                        bookListState.bookList[i].id,
                                        bookListState.bookList[i].title
                                    )
                                }
                        )
                    }
                }
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

