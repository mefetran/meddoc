package mefetran.dgusev.meddocs.test.holder

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import io.cucumber.junit.WithJunitRule
import mefetran.dgusev.meddocs.MainActivity
import org.junit.Rule
import javax.inject.Singleton

@WithJunitRule
@Singleton
class ComposeRuleHolder {
    @get:Rule(order = 0)
    val composeRule = createAndroidComposeRule<MainActivity>()
}
