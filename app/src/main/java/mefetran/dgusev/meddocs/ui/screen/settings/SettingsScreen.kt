package mefetran.dgusev.meddocs.ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mefetran.dgusev.meddocs.R
import mefetran.dgusev.meddocs.ui.components.SettingsExposedDropdownMenu
import mefetran.dgusev.meddocs.ui.screen.settings.model.SettingsState
import mefetran.dgusev.meddocs.ui.theme.MeddocsTheme

@Composable
internal fun SettingsScreen(
    state: SettingsState,
    modifier: Modifier = Modifier,
    onThemeOptionClicked: (String) -> Unit,
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val themesList = remember { listOf(
        context.getString(R.string.theme_system_label),
        context.getString(R.string.theme_dark_label),
        context.getString(R.string.theme_light_label)
    ) }
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .safeDrawingPadding()
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(id = R.string.settings_title),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(32.dp))
            SettingsExposedDropdownMenu(
                options = themesList,
                label = stringResource(id = R.string.theme_label),
                selectedOption = when(state.useSystemTheme) {
                    true -> themesList.first()
                    false -> {
                        if (state.useDarkTheme) {
                            themesList.find { it == stringResource(id = R.string.theme_dark_label) }
                                ?: themesList.first()
                        } else {
                            val theme = themesList.find { it == stringResource(id = R.string.theme_light_label) }
                                ?: themesList.first()
                            theme
                        }
                    }
                },
                onOptionClicked = onThemeOptionClicked,
            )
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
internal fun SettingsScreenPreview(modifier: Modifier = Modifier) {
    MeddocsTheme {
        SettingsScreen(
            state = SettingsState(),
            onThemeOptionClicked = {},
        )
    }
}