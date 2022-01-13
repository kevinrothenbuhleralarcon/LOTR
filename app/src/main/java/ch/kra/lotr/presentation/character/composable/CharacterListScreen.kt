package ch.kra.lotr.presentation.character.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.kra.lotr.R
import ch.kra.lotr.core.ListState
import ch.kra.lotr.core.UIEvent
import ch.kra.lotr.domain.model.character.LotrCharacter
import ch.kra.lotr.presentation.LogoHeader
import ch.kra.lotr.presentation.character.CharacterListEvent
import ch.kra.lotr.presentation.character.viewModel.CharacterViewModel
import kotlinx.coroutines.flow.collect

@Composable
fun CharacterListScreen(
    viewModel: CharacterViewModel,
    navigate: (UIEvent.Navigate) -> Unit
) {
    val characterListState = viewModel.characterList.value
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
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

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                CharacterListWrapper(
                    characterListState = characterListState,
                    modifier = Modifier
                        .weight(0.8f),
                    onEvent = viewModel::onEvent
                )
            }
        }
    }
}

@Composable
private fun CharacterListWrapper(
    characterListState: ListState<LotrCharacter>,
    modifier: Modifier = Modifier,
    onEvent: (CharacterListEvent) -> Unit
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
        if (characterListState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
            )
        } else {
            CharacterList(
                characterList = characterListState.list,
                onEvent = onEvent
            )
        }
    }
}

@Composable
private fun CharacterList(
    characterList: List<LotrCharacter>,
    onEvent: (CharacterListEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.tolkien_characters),
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
            items(characterList.size) { i ->
                if (i > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                }


                Text(
                    text = characterList[i].name,
                    color = MaterialTheme.colors.onSecondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onEvent(CharacterListEvent.DisplayCharacterDetail(characterList[i]))
                        }
                )
            }
        }
    }
}