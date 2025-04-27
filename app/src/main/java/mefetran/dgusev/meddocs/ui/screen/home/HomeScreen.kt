package mefetran.dgusev.meddocs.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mefetran.dgusev.meddocs.ui.theme.MeddocsTheme

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToSignInScreen: () -> Unit,
    navigateToSettingsScreen: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Inside of Home screen",
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
            )
            Button(
                onClick = navigateToSignInScreen,
            ) {
                Text(
                    text = "Navigate to Login screen",
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Button(
                onClick = navigateToSettingsScreen,
            ) {
                Text(
                    text = "Navigate to Settings screen",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
internal fun HomeScreenPreview(modifier: Modifier = Modifier) {
    MeddocsTheme {
        HomeScreen(
            navigateToSignInScreen = {},
            navigateToSettingsScreen = {},
        )
    }
}