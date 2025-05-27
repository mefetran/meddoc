package mefetran.dgusev.meddocs.ui.screen.documents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mefetran.dgusev.meddocs.R
import mefetran.dgusev.meddocs.data.model.Category
import mefetran.dgusev.meddocs.data.model.Document
import mefetran.dgusev.meddocs.ui.components.ScreenTitle
import mefetran.dgusev.meddocs.ui.screen.documents.model.DocumentsState
import java.time.LocalDate
import kotlin.random.Random

@Composable
internal fun DocumentsScreen(
    state: DocumentsState,
    modifier: Modifier = Modifier,
    onNavigateToCreateDocument: () -> Unit,
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
                modifier = Modifier.fillMaxSize(),
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
                items(state.documents) { item ->
                    DocumentsItem(
                        title = item.title,
                        date = item.date,
                        category = Category.entries.firstOrNull { it.name == item.category }
                            ?: Category.Other,
                        description = item.description,
                        onClick = {}
                    )
                }
            }
            FloatingActionButton(
                onClick = onNavigateToCreateDocument,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
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
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.surfaceDim.copy(alpha = 0.8f))
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(72.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "ru")
@Composable
internal fun DocumentsScreenPreview(modifier: Modifier = Modifier) {
    val documents = remember {
        mutableStateListOf(
            Document(
                title = "Общий анализ крови",
                date = LocalDate.now().minusDays(Random.nextLong(1, 5)).toString(),
                description = "Показатели общего анализа крови в норме, без признаков воспаления или инфекции.",
                category = Category.Laboratory.toString(),
            ),
            Document(
                title = "Электрокардиограмма (ЭКГ)",
                date = LocalDate.now().minusDays(Random.nextLong(6, 15)).toString(),
                description = "Результаты ЭКГ в пределах нормы, нарушений ритма не выявлено.",
                category = Category.ECG.toString(),
            ),
            Document(
                title = "УЗИ органов брюшной полости",
                date = LocalDate.now().minusDays(Random.nextLong(16, 30)).toString(),
                description = "Печень, поджелудочная и селезёнка без патологии, размеры в пределах нормы.",
                category = Category.Ultrasound.toString(),
            ),
            Document(
                title = "МРТ головного мозга",
                date = LocalDate.now().minusDays(Random.nextLong(31, 60)).toString(),
                description = "Отсутствие признаков опухолевых образований или очаговых изменений.",
                category = Category.MRI.toString(),
            ),
            Document(
                title = "Эндоскопия желудка (ФГДС)",
                date = LocalDate.now().minusDays(Random.nextLong(61, 100)).toString(),
                description = "Выявлен поверхностный гастрит, биопсия не проводилась.",
                category = Category.Endoscopy.toString(),
            )
        )
    }

    DocumentsScreen(
        state = DocumentsState(
            documents = documents,
//            isLoading = true,
        ),
        onNavigateToCreateDocument = {}
    )
}