package ch.kra.lotr.presentation.character.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
        CharacterListHeader(
            characterName = character?.name ?: "",
            onEvent = viewModel::onEvent
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
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
                character?.let {
                    CharacterDetail(
                        character = it,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        }
    }
}

@Composable
private fun CharacterListHeader(
    characterName: String,
    modifier: Modifier = Modifier,
    onEvent: (CharacterDetailEvent) -> Unit
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
                .clickable { onEvent(CharacterDetailEvent.OnNavigateBack) }
        )

        Spacer(modifier = Modifier.width(32.dp))

        Text(
            text = characterName,
            fontSize = 24.sp,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .offset((0).dp, 16.dp)
        )
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
            annotation = character.wikiUrl,
            start = startIndex,
            end = endIndex
        )
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = character.race,
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
                    character.birth,
            color = MaterialTheme.colors.onSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.death) +
                    character.death,
            color = MaterialTheme.colors.onSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.height) +
                    character.height +
                    " " +
                    stringResource(R.string.cm),
            color = MaterialTheme.colors.onSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.hair) +
                    character.hair,
            color = MaterialTheme.colors.onSecondary
        )


        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.spouse) +
                    character.spouse,
            color = MaterialTheme.colors.onSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.realm) +
                    character.realm,
            color = MaterialTheme.colors.onSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

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