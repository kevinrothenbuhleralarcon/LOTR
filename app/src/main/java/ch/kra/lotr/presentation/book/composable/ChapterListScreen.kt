package ch.kra.lotr.presentation.book.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ch.kra.lotr.R
import ch.kra.lotr.core.ListState
import ch.kra.lotr.core.UIEvent
import ch.kra.lotr.domain.model.book.Chapter
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
            ChapterListHeader(
                bookName = bookName,
                modifier = Modifier.fillMaxWidth(),
                onEvent = viewModel::onEvent
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                ChapterListWrapper(
                    chapterListState = chapterListState
                )
            }
        }
    }
}

@Composable
private fun ChapterListHeader(
    bookName: String,
    modifier: Modifier = Modifier,
    onEvent: (ChapterListEvent) -> Unit
) {
    Row(
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .size(30.dp)
                .offset(16.dp, 16.dp)
                .clickable { onEvent(ChapterListEvent.OnNavigateBackPressed) }
        )
        
        Spacer(modifier = Modifier.width(32.dp))
        
        Text(
            text = bookName,
            fontSize = 24.sp,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .offset((0).dp, 16.dp)
        )
    }
}

@Composable
private fun ChapterListWrapper(
    chapterListState: ListState<Chapter>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .shadow(1.dp, RoundedCornerShape(10.dp))
            .padding(
                end = 8.dp,
                bottom = 8.dp
            )
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.secondary)
            .padding(16.dp)
    ) {
        if (chapterListState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Center)
            )
        } else {
            ChapterList(
                chapterList = chapterListState.list
            )
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
            fontSize = 20.sp
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
                Text(text = "${i + 1}. ${chapterList[i].chapterName}")
            }
        }
    }
}