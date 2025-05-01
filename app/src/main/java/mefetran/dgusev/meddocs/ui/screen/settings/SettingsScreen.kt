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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mefetran.dgusev.meddocs.R
import mefetran.dgusev.meddocs.ui.components.CustomExposedDropdownMenu
import mefetran.dgusev.meddocs.ui.components.model.ThemeOption
import mefetran.dgusev.meddocs.ui.screen.settings.model.SettingsState
import mefetran.dgusev.meddocs.ui.theme.MeddocsTheme

@Composable
internal fun SettingsScreen(
    state: SettingsState,
    modifier: Modifier = Modifier,
    onThemeOptionClicked: (ThemeOption) -> Unit,
) {
    val scrollState = rememberScrollState()
    val themesList = remember {
        listOf(
            ThemeOption.System,
            ThemeOption.Dark,
            ThemeOption.Light,
        )
    }
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
            CustomExposedDropdownMenu(
                options = themesList,
                label = stringResource(id = R.string.theme_label),
                initialValue = when (state.useSystemTheme) {
                    true -> stringResource(id = themesList.first().localizedName)
                    false -> {
                        if (state.useDarkTheme) {
                            stringResource(
                                id = themesList.find { it == ThemeOption.Dark }?.localizedName
                                    ?: themesList.first().localizedName
                            )
                        } else {
                            stringResource(
                                id = themesList.find { it == ThemeOption.Light }?.localizedName
                                    ?: themesList.first().localizedName
                            )
                        }
                    }
                },
                text = { themeOption ->
                    Text(
                        text = stringResource(id = themeOption.localizedName),
                        style = MaterialTheme.typography.bodyLarge
                    )
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