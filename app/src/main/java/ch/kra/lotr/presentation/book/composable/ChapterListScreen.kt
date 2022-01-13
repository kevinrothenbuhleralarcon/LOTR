package ch.kra.lotr.presentation.book.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ch.kra.lotr.R
import ch.kra.lotr.core.UIEvent
import ch.kra.lotr.domain.model.book.Chapter
import ch.kra.lotr.presentation.LoadingWrapper
import ch.kra.lotr.presentation.NavigateBackHeader
import ch.kra.lotr.presentation.book.ChapterListEvent
import ch.kra.lotr.presentation.book.viewmodel.ChapterViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChapterListScreen(
    viewModel: ChapterViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val chapterListState = viewModel.chapterListState.value
    val bookName = viewModel.bookName
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is UIEvent.PopBackStack -> {
                    navigateBack()
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
            NavigateBackHeader(title = bookName) {
                viewModel.onEvent(ChapterListEvent.OnNavigateBackPressed)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                LoadingWrapper(isLoading = chapterListState.isLoading) {
                    ChapterList(chapterList = chapterListState.list)
                }
            }
        }
    }
}

@Composable
fun ChapterList(
    chapterList: List<Chapter>
) {
    Column(modifier = Modifier
        .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.chapters),
            fontSize = 20.sp,
            color = MaterialTheme.colors.onSecondary
        )
        Divider(
            color = MaterialTheme.colors.onSecondary,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(chapterList.size) { i ->
                if (i > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Text(
                    text = "${i + 1}. ${chapterList[i].chapterName}",
                    color = MaterialTheme.colors.onSecondary
                )
            }
        }
    }
}