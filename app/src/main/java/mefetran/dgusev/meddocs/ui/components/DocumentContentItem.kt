package mefetran.dgusev.meddocs.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mefetran.dgusev.meddocs.R

@Composable
internal fun DocumentContentItem(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onDeleteClick: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(all = 8.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "$title:",
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        IconButton(
            onClick = { onDeleteClick(title) },
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = stringResource(id = R.string.delete_button)
            )
        }
    }
}

@Composable
internal fun DocumentContentItem(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(all = 8.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "$title:",
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "ru")
@Composable
fun DocumentContentItemPreview(modifier: Modifier = Modifier) {
    Column {
        DocumentContentItem(
            title = "Состояние слизистой",
            description = "Гиперемирована, очаговая эрозия в антральном отделе",
            onDeleteClick = {},
            modifier = Modifier.padding(16.dp)
        )
        DocumentContentItem(
            title = "Состояние слизистой",
            description = "Гиперемирована, очаговая эрозия в антральном отделе",
            modifier = Modifier.padding(16.dp)
        )
    }
}