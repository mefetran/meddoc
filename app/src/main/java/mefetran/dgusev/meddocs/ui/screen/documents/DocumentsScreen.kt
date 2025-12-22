package mefetran.dgusev.meddocs.ui.screen.documents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mefetran.dgusev.meddocs.R
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.Document
import mefetran.dgusev.meddocs.ui.components.LoadingScreen
import mefetran.dgusev.meddocs.ui.components.ScreenTitle
import mefetran.dgusev.meddocs.ui.screen.documents.model.DocumentsState
import java.time.LocalDate
import java.util.UUID
import kotlin.random.Random

@Composable
internal fun DocumentsScreen(
    state: DocumentsState,
    modifier: Modifier = Modifier,
    onNavigateToCreateDocument: () -> Unit,
    onNavigateToOpenDocument: (String) -> Unit
) {
    val safePaddings = WindowInsets.safeDrawing.asPaddingValues()
    val lazyListState = rememberLazyListState()

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxSize(),
    ) {
        Box {
            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = safePaddings.calculateTopPadding(),
                    bottom = safePaddings.calculateBottomPadding()
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .testTag("documentsTag")
                    .fillMaxSize(),
            ) {
                item {
                    ScreenTitle(
                        title = stringResource(id = R.string.nav_documents),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }
                item {
                    if (state.documents.isEmpty()) {
                        DocumentsEmptyListItem()
                    }
                }
                items(state.documents.sortedByDescending { it.date }) { item ->
                    DocumentsItem(
                        title = item.title,
                        date = item.date,
                        category = item.category,
                        description = item.description,
                        modifier = Modifier.testTag("doc_${item.id}"),
                        onClick = {
                            onNavigateToOpenDocument(item.id)
                        }
                    )
                }
            }
            FloatingActionButton(
                onClick = onNavigateToCreateDocument,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .testTag("createNewDocumentButton")
                    .padding(
                        end = 16.dp,
                        bottom = 16.dp + safePaddings.calculateBottomPadding()
                    )
                    .align(Alignment.BottomEnd)
            ) {
                Text(
                    text = stringResource(id = R.string.create_button),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp)
                )
            }
            if (state.isLoading) {
                LoadingScreen()
            }
        }
    }
}



@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "ru")
@Composable
internal fun DocumentsScreenPreview() {
    val documents = remember {
        mutableStateListOf(
            Document(
                title = "Общий анализ крови",
                date = LocalDate.now().minusDays(Random.nextLong(1, 5)).toString(),
                description = "Показатели общего анализа крови в норме, без признаков воспаления или инфекции.",
                category = Category.Laboratory,
                id = UUID.randomUUID().toString(),
                localFilePath = "",
                file = "",
                priority = 0,
                content = HashMap(),
                createdAt = System.currentTimeMillis().toString(),
                updatedAt = System.currentTimeMillis().toString(),
            ),
            Document(
                title = "Электрокардиограмма (ЭКГ)",
                date = LocalDate.now().minusDays(Random.nextLong(6, 15)).toString(),
                description = "Результаты ЭКГ в пределах нормы, нарушений ритма не выявлено.",
                category = Category.ECG,
                id = UUID.randomUUID().toString(),
                localFilePath = "",
                file = "",
                priority = 0,
                content = HashMap(),
                createdAt = System.currentTimeMillis().toString(),
                updatedAt = System.currentTimeMillis().toString(),
            ),
            Document(
                title = "УЗИ органов брюшной полости",
                date = LocalDate.now().minusDays(Random.nextLong(16, 30)).toString(),
                description = "Печень, поджелудочная и селезёнка без патологии, размеры в пределах нормы.",
                category = Category.Ultrasound,
                id = UUID.randomUUID().toString(),
                localFilePath = "",
                file = "",
                priority = 0,
                content = HashMap(),
                createdAt = System.currentTimeMillis().toString(),
                updatedAt = System.currentTimeMillis().toString(),
            ),
            Document(
                title = "МРТ головного мозга",
                date = LocalDate.now().minusDays(Random.nextLong(31, 60)).toString(),
                description = "Отсутствие признаков опухолевых образований или очаговых изменений.",
                category = Category.MRI,
                id = UUID.randomUUID().toString(),
                localFilePath = "",
                file = "",
                priority = 0,
                content = HashMap(),
                createdAt = System.currentTimeMillis().toString(),
                updatedAt = System.currentTimeMillis().toString(),
            ),
            Document(
                title = "Эндоскопия желудка (ФГДС)",
                date = LocalDate.now().minusDays(Random.nextLong(61, 100)).toString(),
                description = "Выявлен поверхностный гастрит, биопсия не проводилась.",
                category = Category.Endoscopy,
                id = UUID.randomUUID().toString(),
                localFilePath = "",
                file = "",
                priority = 0,
                content = HashMap(),
                createdAt = System.currentTimeMillis().toString(),
                updatedAt = System.currentTimeMillis().toString(),
            )
        )
    }

    DocumentsScreen(
        state = DocumentsState(
            documents = documents,
            isLoading = false,
        ),
        onNavigateToCreateDocument = {},
        onNavigateToOpenDocument = {},
    )
}
