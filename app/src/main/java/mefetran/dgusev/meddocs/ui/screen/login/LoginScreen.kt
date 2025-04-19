package mefetran.dgusev.meddocs.ui.screen.login

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
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigateToHomeScreen: () -> Unit,
    navigateToRegistrationScreen: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Inside of Login screen",
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
            )
            Button(
                onClick = navigateToHomeScreen,
            ) { 
                Text(
                    text = "Navigate to Home screen",
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Button(
                onClick = navigateToRegistrationScreen,
            ) {
                Text(
                    text = "Navigate to Registration screen",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
fun LoginScreenPreview(modifier: Modifier = Modifier) {
    MeddocsTheme {
        LoginScreen(
            navigateToHomeScreen = {},
            navigateToRegistrationScreen = {},
        )
    }
}