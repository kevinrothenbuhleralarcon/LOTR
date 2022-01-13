package ch.kra.lotr.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.kra.lotr.R

@Composable
fun LogoHeader(modifier: Modifier = Modifier) {
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
fun NavigateBackHeader(
    title: String,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
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
                .clickable { navigateBack() }
        )

        Spacer(modifier = Modifier.width(32.dp))

        Text(
            text = title,
            fontSize = 24.sp,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .offset((0).dp, 16.dp)
        )
    }
}

@Composable
fun LoadingWrapper(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    composable: @Composable() () -> Unit,
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
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
            )
        } else {
            composable()
        }
    }
}