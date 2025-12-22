package mefetran.dgusev.meddocs.ui.screen.documents

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.atiurin.ultron.core.compose.createUltronComposeRule
import com.atiurin.ultron.core.compose.nodeinteraction.click
import com.atiurin.ultron.extensions.assertExists
import com.atiurin.ultron.extensions.assertIsDisplayed
import com.atiurin.ultron.page.Page
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import mefetran.dgusev.meddocs.HiltTestActivity
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.Document
import mefetran.dgusev.meddocs.ui.screen.documents.model.DocumentsState
import mefetran.dgusev.meddocs.ui.theme.MeddocsTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertTrue

object DocumentsPage : Page<DocumentsPage>() {
    private val documentsList = mutableStateListOf<Document>()
    private val documentListMatcher = hasTestTag("documentsTag")
    private val createDocumentMatcher = hasTestTag("createNewDocumentButton")
    private var isCreateDocClicked = false
    private var isOpenDocClicked = false

    private fun assertDocumentsListExistAndDisplayed() {
        documentListMatcher
            .assertExists()
            .assertIsDisplayed()
    }

    fun scrollToDocumentByTestTag(testTag: String) {
        // act
        assertDocumentsListExistAndDisplayed()
        hasTestTag(testTag)
            .assertExists()
            .scrollTo()

        // assert
        hasTestTag(testTag).assertIsDisplayed()
    }

    fun setupDocumentsList() {
        documentsList.clear()
        val tempDocumentsList = listOf(
            Document(
                id = "DOC-001",
                title = "Общий анализ крови",
                description = "Результаты клинического анализа крови",
                date = "2024-01-15",
                localFilePath = "/medical/results/blood_test_001.pdf",
                file = "blood_test_001.pdf",
                category = Category.Laboratory,
                priority = 1,
                content = mapOf(
                    "лейкоциты" to "6.5 × 10⁹/л",
                    "эритроциты" to "4.8 × 10¹²/л",
                    "гемоглобин" to "145 г/л",
                    "тромбоциты" to "220 × 10⁹/л"
                ),
                createdAt = "2024-01-15T10:30:00",
                updatedAt = "2024-01-15T10:30:00"
            ),
            Document(
                id = "DOC-002",
                title = "Эхокардиография сердца",
                description = "Ультразвуковое исследование сердца и сосудов",
                date = "2024-01-20",
                localFilePath = "/medical/results/echo_heart_002.pdf",
                file = "echo_heart_002.pdf",
                category = Category.Ultrasound,
                priority = 2,
                content = mapOf(
                    "фракция выброса" to "65%",
                    "размер левого желудочка" to "48 мм",
                    "толщина межжелудочковой перегородки" to "10 мм"
                ),
                createdAt = "2024-01-20T14:15:00",
                updatedAt = "2024-01-20T14:15:00"
            ),
            Document(
                id = "DOC-003",
                title = "Рентген грудной клетки",
                description = "Рентгенологическое исследование органов грудной клетки",
                date = "2024-01-25",
                localFilePath = "/medical/results/xray_chest_003.jpg",
                file = "xray_chest_003.jpg",
                category = Category.XRay,
                priority = 2,
                content = mapOf(
                    "заключение" to "Легкие без очаговых и инфильтративных изменений",
                    "сердце" to "Размеры в норме",
                    "диафрагма" to "Расположена обычно"
                ),
                createdAt = "2024-01-25T09:45:00",
                updatedAt = "2024-01-25T09:45:00"
            ),
            Document(
                id = "DOC-004",
                title = "МРТ головного мозга",
                description = "Магнитно-резонансная томография головного мозга",
                date = "2024-02-01",
                localFilePath = "/medical/results/mri_brain_004.dcm",
                file = "mri_brain_004.dcm",
                category = Category.MRI,
                priority = 3,
                content = mapOf(
                    "заключение" to "Очаговых изменений вещества мозга не выявлено",
                    "желудочковая система" to "Не расширена",
                    "базальные цистерны" to "Не изменены"
                ),
                createdAt = "2024-02-01T11:20:00",
                updatedAt = "2024-02-01T11:20:00"
            ),
            Document(
                id = "DOC-005",
                title = "КТ брюшной полости",
                description = "Компьютерная томография органов брюшной полости",
                date = "2024-02-05",
                localFilePath = "/medical/results/ct_abdomen_005.dcm",
                file = "ct_abdomen_005.dcm",
                category = Category.CT,
                priority = 3,
                content = mapOf(
                    "печень" to "Размеры и структура в норме",
                    "поджелудочная железа" to "Без особенностей",
                    "почки" to "Обычной локализации и размеров"
                ),
                createdAt = "2024-02-05T16:30:00",
                updatedAt = "2024-02-05T16:30:00"
            ),
            Document(
                id = "DOC-006",
                title = "Электрокардиограмма",
                description = "Суточное мониторирование ЭКГ по Холтеру",
                date = "2024-02-10",
                localFilePath = "/medical/results/ecg_holter_006.pdf",
                file = "ecg_holter_006.pdf",
                category = Category.ECG,
                priority = 2,
                content = mapOf(
                    "ритм" to "Синусовый",
                    "ЧСС" to "68-85 уд/мин",
                    "экстрасистолы" to "Не зарегистрированы"
                ),
                createdAt = "2024-02-10T08:00:00",
                updatedAt = "2024-02-10T08:00:00"
            ),
            Document(
                id = "DOC-007",
                title = "ФГДС",
                description = "Фиброгастродуоденоскопия с биопсией",
                date = "2024-02-15",
                localFilePath = "/medical/results/endoscopy_007.pdf",
                file = "endoscopy_007.pdf",
                category = Category.Endoscopy,
                priority = 2,
                content = mapOf(
                    "пищевод" to "Свободно проходим, слизистая не изменена",
                    "желудок" to "Слизистая умеренно гиперемирована",
                    "биопсия" to "Взята из антрального отдела"
                ),
                createdAt = "2024-02-15T13:45:00",
                updatedAt = "2024-02-15T13:45:00"
            ),
            Document(
                id = "DOC-008",
                title = "Спирометрия",
                description = "Исследование функции внешнего дыхания",
                date = "2024-02-20",
                localFilePath = "/medical/results/spirometry_008.pdf",
                file = "spirometry_008.pdf",
                category = Category.FunctionalDiagnostics,
                priority = 1,
                content = mapOf(
                    "ЖЕЛ" to "4.2 л (95% от должной)",
                    "ФЖЕЛ" to "4.0 л (93% от должной)",
                    "ОФВ1" to "3.5 л (96% от должной)"
                ),
                createdAt = "2024-02-20T10:10:00",
                updatedAt = "2024-02-20T10:10:00"
            ),
            Document(
                id = "DOC-009",
                title = "Биохимический анализ крови",
                description = "Расширенный биохимический анализ",
                date = "2024-02-25",
                localFilePath = "/medical/results/biochem_009.pdf",
                file = "biochem_009.pdf",
                category = Category.Laboratory,
                priority = 1,
                content = mapOf(
                    "глюкоза" to "5.3 ммоль/л",
                    "холестерин" to "5.1 ммоль/л",
                    "АЛТ" to "28 Ед/л",
                    "креатинин" to "88 мкмоль/л"
                ),
                createdAt = "2024-02-25T12:00:00",
                updatedAt = "2024-02-25T12:00:00"
            ),
            Document(
                id = "DOC-010",
                title = "Консультация специалиста",
                description = "Заключение врача-терапевта",
                date = "2024-03-01",
                localFilePath = "/medical/results/consult_010.pdf",
                file = "consult_010.pdf",
                category = Category.Other,
                priority = 1,
                content = mapOf(
                    "диагноз" to "Гипертоническая болезнь I стадии",
                    "рекомендации" to "Диета, контроль АД, наблюдение",
                    "следующий визит" to "Через 3 месяца"
                ),
                createdAt = "2024-03-01T15:30:00",
                updatedAt = "2024-03-01T15:30:00"
            )
        )
        documentsList.addAll(tempDocumentsList)
    }

    fun setupDocumentsScreen(composeRule: AndroidComposeTestRule<ActivityScenarioRule<HiltTestActivity>, HiltTestActivity>) {
        // arrange
        isOpenDocClicked = false
        isCreateDocClicked = false
        composeRule.setContent {
            MeddocsTheme {
                val state = DocumentsState(documents = documentsList)

                DocumentsScreen(
                    state = state,
                    onNavigateToCreateDocument = {
                        isCreateDocClicked = true
                    },
                    onNavigateToOpenDocument = {
                        isOpenDocClicked = true
                    },
                )
            }
        }
    }

    fun clickOnCreateDocButton() {
        // act
        assertDocumentsListExistAndDisplayed()
        createDocumentMatcher
            .assertExists()
            .assertIsDisplayed()
            .click()

        // assert
        assertTrue(isCreateDocClicked, "Create document was not clicked!")
    }

    fun clickOnDocumentToOpenItByTestTag(testTag: String) {
        // act
        hasTestTag(testTag)
            .assertExists()
            .assertIsDisplayed()
            .click()

        // assert
        assertTrue(isOpenDocClicked, "Document was not clicked!")
    }
}

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DocumentsScreenUltronTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createUltronComposeRule<HiltTestActivity>()

    @Before
    fun setup() {
        DocumentsPage {
            setupDocumentsList()
            setupDocumentsScreen(composeRule)
        }
    }

    @Test
    fun shouldScrollToConcreteDocument() {
        DocumentsPage {
            scrollToDocumentByTestTag("doc_DOC-010")
        }
    }

    @Test
    fun shouldClickOnCreateDocumentButton() {
        DocumentsPage {
            clickOnCreateDocButton()
        }
    }

    @Test
    fun shouldClickOnDocumentToOpenIt() {
        DocumentsPage {
            scrollToDocumentByTestTag("doc_DOC-010")
            clickOnDocumentToOpenItByTestTag("doc_DOC-010")
        }
    }
}