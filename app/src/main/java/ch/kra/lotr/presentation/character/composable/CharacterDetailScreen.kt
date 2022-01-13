package ch.kra.lotr.presentation.character.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.kra.lotr.R
import ch.kra.lotr.core.UIEvent
import ch.kra.lotr.domain.model.character.LotrCharacter
import ch.kra.lotr.presentation.LoadingWrapper
import ch.kra.lotr.presentation.NavigateBackHeader
import ch.kra.lotr.presentation.character.CharacterDetailEvent
import ch.kra.lotr.presentation.character.viewModel.CharacterViewModel
import kotlinx.coroutines.flow.collect

@Composable
fun CharacterDetailScreen(
    viewModel: CharacterViewModel,
    navigateBack: () -> Unit
) {
    val character = viewModel.character
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.PopBackStack -> {
                    navigateBack()
                }

                is UIEvent.StartIntent -> {
                    context.startActivity(event.intent)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
    ) {
        NavigateBackHeader(title = character?.name ?: "") {
            viewModel.onEvent(CharacterDetailEvent.OnNavigateBack)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LoadingWrapper(isLoading = false) {
                character?.let { character ->
                    CharacterDetail(
                        character = character,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        }
    }
}

@Composable
private fun CharacterDetail (
    character: LotrCharacter,
    onEvent: (CharacterDetailEvent) -> Unit
) {
    val icon = if (character.gender == "Female") {
        ImageVector.vectorResource(id = R.drawable.ic_female)
    } else {
        ImageVector.vectorResource(id = R.drawable.ic_male)
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = character.race ?: stringResource(R.string.unknown),
                color = MaterialTheme.colors.onSecondary
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Icon(
                imageVector = icon,
                contentDescription = character.gender,
                tint = MaterialTheme.colors.onSecondary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.born) +
                    (character.birth ?: stringResource(R.string.unknown)),
            color = MaterialTheme.colors.onSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.death) +
                    (character.death ?: stringResource(R.string.unknown)),
            color = MaterialTheme.colors.onSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        val heightStringBuilder = StringBuilder()
        heightStringBuilder.append(stringResource(R.string.height))
        character.height?.let { height ->
            heightStringBuilder.append(height)
            heightStringBuilder.append(" ")
            heightStringBuilder.append(stringResource(R.string.cm))
        } ?: heightStringBuilder.append(stringResource(id = R.string.unknown))

        Text(
            text = heightStringBuilder.toString(),
            color = MaterialTheme.colors.onSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.hair) +
                    (character.hair ?: stringResource(id = R.string.unknown)),
            color = MaterialTheme.colors.onSecondary
        )


        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.spouse) +
                    (character.spouse ?: stringResource(id = R.string.unknown)),
            color = MaterialTheme.colors.onSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.realm) +
                    (character.realm ?: stringResource(id = R.string.unknown)),
            color = MaterialTheme.colors.onSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        character.wikiUrl?.let { url ->
            val annotatedLinkString: AnnotatedString = buildAnnotatedString {
                val str = stringResource(R.string.wiki)
                val startIndex = 0
                val endIndex = str.length
                append(str)
                addStyle(
                    style = SpanStyle(
                        color = Color(0xff64B5F6),
                        fontSize = 18.sp,
                        textDecoration = TextDecoration.Underline
                    ), start = startIndex, end = endIndex
                )
                addStringAnnotation(
                    tag = "URL",
                    annotation = url,
                    start = startIndex,
                    end = endIndex
                )
            }

            ClickableText(
                text = annotatedLinkString,
                onClick = {
                    annotatedLinkString
                        .getStringAnnotations("URL", it, it)
                        .firstOrNull()?.let { stringAnnotation ->
                            onEvent(CharacterDetailEvent.OnNavigateToWiki(stringAnnotation.item))
                        }
                }
            )
        }
    }
}