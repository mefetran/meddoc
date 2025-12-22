package mefetran.dgusev.meddocs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    opacity: Float = 0.8f,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceDim.copy(opacity))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {})
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 6.dp,
            modifier = Modifier.size(72.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "ru")
@Composable
fun LoadingScreenPreview(modifier: Modifier = Modifier) {
    LoadingScreen()
}