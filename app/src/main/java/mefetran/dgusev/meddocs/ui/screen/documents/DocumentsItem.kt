package mefetran.dgusev.meddocs.ui.screen.documents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mefetran.dgusev.meddocs.data.model.Category
import mefetran.dgusev.meddocs.data.model.Document
import mefetran.dgusev.meddocs.data.model.icon
import mefetran.dgusev.meddocs.ui.theme.MeddocsTheme
import java.time.LocalDate

@Composable
fun DocumentsItem(
    title: String,
    date: String,
    category: Category,
    modifier: Modifier = Modifier,
    description: String = "",
    onClick: () -> Unit,
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        onClick = onClick,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = date,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = category.icon(),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(id = category.labelRes),
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                )
            }
            if (description.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, locale = "ru")
@Composable
fun DocumentsItemPreview(modifier: Modifier = Modifier) {
    val document = remember {
        Document(
            title = "Общий анализ крови",
            date = LocalDate.now().toString(),
            description = "Показатели общего анализа крови в норме, без признаков воспаления или инфекции.",
            category = Category.Laboratory.toString(),
        )
    }
    MeddocsTheme {
        DocumentsItem(
            title = document.title,
            date = document.date,
            description = document.description,
            category = if (document.category == Category.Laboratory.toString()) Category.Laboratory else Category.Other,
            modifier = Modifier.padding(all = 16.dp),
            onClick = {},
        )
    }
}