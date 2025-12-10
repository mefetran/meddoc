package mefetran.dgusev.meddocs.test.steps

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import dagger.hilt.android.testing.HiltAndroidTest
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import mefetran.dgusev.meddocs.test.holder.ComposeRuleHolder

@HiltAndroidTest
class OpenDocumentSteps(
    val composeRuleHolder: ComposeRuleHolder,
) : SemanticsNodeInteractionsProvider by composeRuleHolder.composeRule {

    val rule = composeRuleHolder.composeRule

    @Given("I see list of documents")
    fun waitingListOfDocuments() {
        rule.waitForIdle()
        rule.onNodeWithTag("documentsNavBarTag")
            .assertExists()
        rule.onNodeWithTag("documentsNavBarTag")
            .assertIsDisplayed()
    }

    @When("I tap on document with name {string}")
    fun onDocumentClick(documentName: String) {
        val nodes = rule.onAllNodesWithText(documentName)

        nodes.fetchSemanticsNodes().firstOrNull()
            ?: throw AssertionError("No document with name '$documentName' was found")

        val firstNode = nodes[0]

        firstNode.performScrollTo()
        firstNode.performClick()
    }

    @Then("I should see Open Document screen")
    fun openDocumentScreen() {
        rule.waitForIdle()
        rule.onNodeWithTag("screenTitleDocumentTag")
            .assertExists()
            .assertIsDisplayed()
        Thread.sleep(1000L)
        rule.onNodeWithTag("openDocBackClickTag", useUnmergedTree = true)
            .performClick()
    }
}