package mefetran.dgusev.meddocs.ui.screen.documentopen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import io.github.kakaocup.compose.node.element.KNode
import mefetran.dgusev.meddocs.HiltTestActivity
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.Document
import mefetran.dgusev.meddocs.ui.screen.documentopen.model.OpenDocumentState
import mefetran.dgusev.meddocs.ui.theme.MeddocsTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OpenDocumentScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
ComposeScreen<OpenDocumentScreen>(
    semanticsProvider = semanticsProvider,
    viewBuilderAction = { hasTestTag("OpenDocumentScreenTag") }
) {
    val screenTitle: KNode = child {
        hasTestTag("screenTitleDocumentTag")
    }
}

@HiltAndroidTest
class OpenDocumentScreenKaspressoTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<HiltTestActivity>()

    @Before
    fun setup() {

        val state = OpenDocumentState(
            document =
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

        composeRule.setContent {
            MeddocsTheme {
                OpenDocumentScreen(
                    state = state,
                    onBackClick = {},
                    onDeleteClick = {},
                    onPdfFileSelected = {}
                )
            }
        }
    }

    @Test
    fun shouldSeeTitleOfOpenedDocument() = run {
        step("Check that title is displayed") {
            onComposeScreen<OpenDocumentScreen>(composeRule) {
                screenTitle.assertIsDisplayed()
            }
        }
        step("Make a screenshot that shows a title") {
            composeRule.waitForIdle()
            device.permissions.allowViaDialog()
            device.screenshots.take("ShowScreenTitle")
        }
    }
}