package mefetran.dgusev.meddocs.test.steps

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import dagger.hilt.android.testing.HiltAndroidTest
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import mefetran.dgusev.meddocs.test.holder.ComposeRuleHolder

@HiltAndroidTest
class LogoutSteps(
    val composeRuleHolder: ComposeRuleHolder,
) : SemanticsNodeInteractionsProvider by composeRuleHolder.composeRule {

    val rule = composeRuleHolder.composeRule

    @Given("I see settings button")
    fun waitingForSettingsButton() {
        rule.waitForIdle()
        rule.onNodeWithTag("settingsNavBarTag")
            .assertExists()
            .assertIsDisplayed()
    }

    @When("I tap on it")
    fun onSettingsNavButtonClick() {
        rule.onNodeWithTag("settingsNavBarTag").performClick()
    }

    @Then("I should see Settings screen")
    fun waitingForSettingsScreen() {
        rule.onNodeWithTag("logoutButtonTag")
            .performScrollTo()
            .assertExists()
            .assertIsDisplayed()
    }

    @When("I tap on logout button")
    fun onLogoutClick() {
        rule.onNodeWithTag("logoutButtonTag").performClick()
    }

    @Then("I should see Login screen")
    fun inSignInScreen(){
        rule.waitForIdle()
        rule.onNodeWithTag("usernameInput")
            .assertExists()
            .assertIsDisplayed()
    }
}
