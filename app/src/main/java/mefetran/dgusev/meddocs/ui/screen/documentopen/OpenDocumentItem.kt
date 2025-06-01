package mefetran.dgusev.meddocs.ui.screen.documentopen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mefetran.dgusev.meddocs.R

@Composable
internal fun OpenDocumentItem(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "$title: ",
            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary),
            modifier = Modifier.alignByBaseline()
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .alignByBaseline()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "ru")
@Composable
internal fun OpenDocumentItemPreview(modifier: Modifier = Modifier) {
    OpenDocumentItem(
        modifier = Modifier.padding(16.dp),
        title = stringResource(id = R.string.description_label),
        description = "Выявлен поверхностный гастрит, биопсия не проводилась.",
    )
}