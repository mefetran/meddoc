package mefetran.dgusev.meddocs.ui.screen.documentopen

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import mefetran.dgusev.meddocs.R
import mefetran.dgusev.meddocs.app.PDF_MIME_TYPE
import mefetran.dgusev.meddocs.app.decryptPdfToTempFile
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.Document
import mefetran.dgusev.meddocs.ui.components.AppToolbar
import mefetran.dgusev.meddocs.ui.components.DocumentContentItem
import mefetran.dgusev.meddocs.ui.components.LoadingScreen
import mefetran.dgusev.meddocs.ui.components.ScreenTitle
import mefetran.dgusev.meddocs.ui.components.getLabelRes
import mefetran.dgusev.meddocs.ui.components.icon
import mefetran.dgusev.meddocs.ui.screen.documentopen.model.OpenDocumentState
import java.io.File
import java.time.LocalDate
import kotlin.random.Random

@Composable
internal fun OpenDocumentScreen(
    state: OpenDocumentState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onPdfFileSelected: (Uri) -> Unit,
) {
    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            onPdfFileSelected(it)
        }
    }
    val context = LocalContext.current

    BackHandler { onBackClick() }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxSize(),
    ) {
        when (state.isError) {
            true -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ) {
                    AppToolbar(
                        onBackClick = onBackClick,
                        modifier = Modifier.padding(
                            top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
                        )
                    )
                    ScreenTitle(title = stringResource(id = R.string.error_loading_document_title))
                    Spacer(Modifier.height(28.dp))
                    DocumentContentItem(
                        title = stringResource(id = R.string.description_label),
                        description = stringResource(id = R.string.error_loading_document_description),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            false -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ) {
                    AppToolbar(
                        onBackClick = onBackClick,
                        modifier = Modifier.padding(
                            top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.weight(1f)
                        ) {
                            if (state.document.localFilePath.isBlank()) {
                                IconButton(
                                    onClick = {
                                        pdfPickerLauncher.launch(arrayOf(PDF_MIME_TYPE))
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AttachFile,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                            if (state.document.localFilePath.isNotBlank()) {
                                IconButton(
                                    onClick = {
                                        val encryptedFile = File(state.document.localFilePath)
                                        val decryptedFile = decryptPdfToTempFile(context, encryptedFile)

                                        val uri = FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.provider",
                                            decryptedFile,
                                        )
                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                            setDataAndType(uri, PDF_MIME_TYPE)
                                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                        }
                                        context.startActivity(intent)
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PictureAsPdf,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                            IconButton(
                                onClick = onDeleteClick
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                    Column(Modifier.padding(horizontal = 16.dp)) {
                        ScreenTitle(title = state.document.title)
                        Spacer(Modifier.height(28.dp))
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    stringResource(id = state.document.category.getLabelRes()),
                                    style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onPrimary)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    state.document.category.icon(),
                                    null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                        )
                        if (state.document.description.isNotBlank()) {
                            OpenDocumentItem(
                                title = stringResource(id = R.string.description_label),
                                description = state.document.description,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                        if (state.document.date.isNotBlank()) {
                            OpenDocumentItem(
                                title = stringResource(id = R.string.date_label),
                                description = state.document.date,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                        if (state.document.content.isNotEmpty()) {
                            Text(
                                stringResource(id = R.string.additional_data_label),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            state.document.content.forEach { (title, description) ->
                                DocumentContentItem(
                                    title = title,
                                    description = description,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        if (state.isLoading) {
            LoadingScreen(opacity = 1f)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "ru")
@Composable
internal fun OpenDocumentScreenPreview() {
    OpenDocumentScreen(
        state = OpenDocumentState(
            isError = false,
            document = Document(
                title = "Эндоскопия желудка (ФГДС)",
                date = LocalDate.now().minusDays(Random.nextLong(61, 100)).toString(),
                description = "Выявлен поверхностный гастрит, биопсия не проводилась.",
                category = Category.Endoscopy,
                content = mapOf(
                    "Состояние слизистой" to "Гиперемирована, очаговая эрозия в антральном отделе",
                    "Кардиальный жом" to "Закрывается неполностью",
                    "Перистальтика" to "Сохранена",
                    "Пилорический отдел" to "Проходим, деформирован",
                    "Двенадцатиперстная кишка" to "Без видимых патологий",
                    "Наличие желчи" to "Присутствует в просвете желудка",
                    "Биопсия" to "Не проводилась"
                ),
                id = "0",
                file = "",
                localFilePath = "",
                priority = 0,
                createdAt = "",
                updatedAt = "",
            ),
            isLoading = false,
        ),
        onBackClick = {},
        onDeleteClick = {},
        onPdfFileSelected = {},
    )
}
