package mefetran.dgusev.meddocs.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mefetran.dgusev.meddocs.ui.theme.MeddocsTheme

@Composable
fun BackToolbar(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .requiredHeight(56.dp)
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = onBackClicked
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back button",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "ru")
@Composable
fun BackToolbarPreview(modifier: Modifier = Modifier) {
    MeddocsTheme {
        BackToolbar(
            modifier = Modifier.padding(16.dp),
            onBackClicked = {},
        )
    }
}