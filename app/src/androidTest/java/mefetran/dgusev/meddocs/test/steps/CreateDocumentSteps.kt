package mefetran.dgusev.meddocs.test.steps

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import dagger.hilt.android.testing.HiltAndroidTest
import io.cucumber.java.After
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import mefetran.dgusev.meddocs.test.holder.ComposeRuleHolder

@HiltAndroidTest
class CreateDocumentSteps(
    val composeRuleHolder: ComposeRuleHolder,
) : SemanticsNodeInteractionsProvider by composeRuleHolder.composeRule {
    val rule = composeRuleHolder.composeRule

    @Given("I see document home screen")
    fun seeHomeScreen() {
        rule.waitForIdle()
        rule.onNodeWithTag("documentsNavBarTag")
            .assertExists()
        rule.onNodeWithTag("documentsNavBarTag")
            .assertIsDisplayed()
    }

    @When("I tap on create document icon")
    fun tapOnCreateDocIcon() {
        rule.onNodeWithTag("createNewDocumentButton")
            .assertExists()
        rule.onNodeWithTag("createNewDocumentButton")
            .assertIsDisplayed()
        rule.onNodeWithTag("createNewDocumentButton")
            .performClick()
    }

    @Then("I should see create document screen")
    fun openCreateDocumentScreen() {
        rule.waitForIdle()
        rule.onNodeWithTag("nameOfNewDocTag")
            .assertIsDisplayed()
    }

    @When("I type name {string} of document")
    fun enterNameOfDoc(name: String) {
        rule.onNodeWithTag("nameOfNewDocTag")
            .performTextClearance()
        rule.onNodeWithTag("nameOfNewDocTag")
            .performTextInput(name)
    }

    @And("I type description {string}")
    fun enterDescOfDoc(description: String) {
        rule.onNodeWithTag("descriptionOfNewDocTag")
            .performTextClearance()
        rule.onNodeWithTag("descriptionOfNewDocTag")
            .performTextInput(description)
    }

    @And("I type in add info key {string}")
    fun enterKeyAddInfo(key: String) {
        rule.onNodeWithTag("keyAddInfoTag")
            .performScrollTo()
            .assertIsDisplayed()
        rule.onNodeWithTag("keyAddInfoTag")
            .performTextClearance()
        rule.onNodeWithTag("keyAddInfoTag")
            .performTextInput(key)
    }

    @And("I type in add info value {string}")
    fun enterValueAddInfo(value: String) {
        rule.onNodeWithTag("valueAddInfoTag")
            .performScrollTo()
            .assertIsDisplayed()
        rule.onNodeWithTag("valueAddInfoTag")
            .performTextClearance()
        rule.onNodeWithTag("valueAddInfoTag")
            .performTextInput(value)
    }

    @And("I tap on Add button")
    fun addKeyValueAddInfo() {
        rule.onNodeWithTag("buttonAddInfoTag")
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()
    }

    @When("I tap create document button")
    fun createDocument() {
        rule.onNodeWithTag("buttonCreateDocumentTag")
            .assertIsDisplayed()
            .performClick()
    }
}