package mefetran.dgusev.meddocs.test.steps

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import dagger.hilt.android.testing.HiltAndroidTest
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import mefetran.dgusev.meddocs.test.holder.ComposeRuleHolder

@HiltAndroidTest
class LoginSteps(
    val composeRuleHolder: ComposeRuleHolder,
) : SemanticsNodeInteractionsProvider by composeRuleHolder.composeRule {

    val rule = composeRuleHolder.composeRule

    @Given("I open the login screen")
    fun openLoginScreen() {
        rule.waitForIdle()
    }

    @When("I type username {string}")
    fun inputUsername(username: String) {
        rule.onNodeWithTag("usernameInput").performTextClearance()
        rule.onNodeWithTag("usernameInput").performTextInput(username)
    }

    @And("I type password {string}")
    fun inputPassword(password: String) {
        rule.onNodeWithTag("passwordInput").performTextClearance()
        rule.onNodeWithTag("passwordInput").performTextInput(password)
    }

    @And("I tap the login button")
    fun login() {
        rule.onNodeWithTag("loginButton").performClick()
    }

    @Then("I should see Home screen")
    fun homeScreen() {
        rule.waitForIdle()
        rule.onNodeWithTag("documentsNavBarTag").assertIsDisplayed()
    }
}
