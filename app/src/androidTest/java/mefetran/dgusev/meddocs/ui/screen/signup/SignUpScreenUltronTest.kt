package mefetran.dgusev.meddocs.ui.screen.signup

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.text.input.TextFieldValue
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.atiurin.ultron.core.compose.createUltronComposeRule
import com.atiurin.ultron.extensions.assertExists
import com.atiurin.ultron.extensions.withUseUnmergedTree
import com.atiurin.ultron.page.Page
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import mefetran.dgusev.meddocs.HiltTestActivity
import mefetran.dgusev.meddocs.ui.screen.signin.EMAIL_VALUE
import mefetran.dgusev.meddocs.ui.screen.signin.PASSWORD_VALUE
import mefetran.dgusev.meddocs.ui.screen.signup.model.SignUpState
import mefetran.dgusev.meddocs.ui.theme.MeddocsTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

object SignUpPage : Page<SignUpPage>() {
    private val signUpState = MutableStateFlow(SignUpState())
    private val emailState = MutableStateFlow(TextFieldValue(""))
    private val passwordState = MutableStateFlow(TextFieldValue(""))
    private val nameState = MutableStateFlow(TextFieldValue(""))
    private var isSignUpClicked = false
    private var isBackButtonClicked = false
    private val emailErrorMessageMatcher = hasTestTag("emailErrorSignUpTag")
    private val passwordErrorMessageMatcher = hasTestTag("passwordErrorSignUpTag")
    private val emailMatcher = hasTestTag("emailTestTag")
    private val passwordMatcher = hasTestTag("passwordTestTag")

    fun setupSignUpScreen(composeRule: AndroidComposeTestRule<ActivityScenarioRule<HiltTestActivity>, HiltTestActivity>) {
        // arrange
        composeRule.setContent {
            val state by signUpState.collectAsState()
            val emailValue by emailState.collectAsState()
            val passwordValue by passwordState.collectAsState()
            val nameValue by nameState.collectAsState()

            MeddocsTheme {
                MeddocsTheme {
                    SignUpScreen(
                        state = state,
                        emailValue = emailValue,
                        passwordValue = passwordValue,
                        nameValue = nameValue,
                        onSignUp = { isSignUpClicked = true },
                        onNewEmailValue = { newValue ->
                            emailState.update { newValue }
                        },
                        onNewPasswordValue = { newValue ->
                            passwordState.update { newValue }
                        },
                        onNewNameValue = { newValue ->
                            nameState.update { newValue }
                        },
                        onShowPasswordClicked = {
                            signUpState.update { it.copy(showPassword = !it.showPassword) }
                        },
                        onBackClicked = { isBackButtonClicked = true },
                    )
                }

            }
        }
    }

    fun showErrorMessages() {
        // act
        signUpState.update {
            it.copy(
                isEmailError = true,
                isPasswordShortError = true,
            )
        }

        // assert
        emailErrorMessageMatcher
            .withUseUnmergedTree(true)
            .assertExists()
            .assertIsDisplayed()
        passwordErrorMessageMatcher
            .withUseUnmergedTree(true)
            .assertExists()
            .assertIsDisplayed()
    }

    fun setupSignUpState() {
        isBackButtonClicked = false
        isSignUpClicked = false
        signUpState.update { SignUpState() }
        emailState.update { TextFieldValue("") }
        passwordState.update { TextFieldValue("") }
        nameState.update { TextFieldValue("") }
    }

    fun enterTestEmail() {
        // act
        emailMatcher
            .assertExists()
            .assertIsDisplayed()
            .clearText()
            .inputText(EMAIL_VALUE)

        // asssert
        assertEquals(EMAIL_VALUE, emailState.value.text, "Email is not correct!")
    }

    fun enterTestPassword() {
        // act
        passwordMatcher
            .assertExists()
            .assertIsDisplayed()
            .clearText()
            .inputText(PASSWORD_VALUE)

        // asssert
        assertEquals(PASSWORD_VALUE, passwordState.value.text, "Password is not correct!")
    }
}

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SignUpScreenUltronTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createUltronComposeRule<HiltTestActivity>()

    @Before
    fun setup() {
        SignUpPage {
            setupSignUpState()
            setupSignUpScreen(composeRule)
        }
    }

    @Test
    fun shouldShowErrorMessagesWhenStateInErrorState() {
        SignUpPage {
            showErrorMessages()
        }
    }

    @Test
    fun shouldProperlyEnterEmailAndPasswordValue() {
        SignUpPage {
            enterTestEmail()
            enterTestPassword()
        }
    }
}