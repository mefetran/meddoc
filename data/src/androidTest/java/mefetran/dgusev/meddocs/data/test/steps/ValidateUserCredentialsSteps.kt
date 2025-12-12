package mefetran.dgusev.meddocs.data.test.steps

import android.util.Log
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlinx.coroutines.test.runTest
import mefetran.dgusev.meddocs.data.model.PASSWORD_MAX_LENGTH
import mefetran.dgusev.meddocs.data.model.PASSWORD_MIN_LENGTH
import mefetran.dgusev.meddocs.data.usecase.user.ValidateUserCredentialsUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.ValidateResult
import mefetran.dgusev.meddocs.domain.usecase.user.ValidateUserCredentialsUseCase
import org.junit.Assume.assumeTrue
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidateUserCredentialsSteps {

    private lateinit var useCase: ValidateUserCredentialsUseCase
    private var result: ValidateResult? = null

    // constants
    private var minLength: Int = PASSWORD_MIN_LENGTH
    private var maxLength: Int = PASSWORD_MAX_LENGTH

    @Given("a ValidateUserCredentialsUseCase")
    fun initUseCase() {
        useCase = ValidateUserCredentialsUseCaseImpl()
    }

    @And("PASSWORD_MIN_LENGTH = {int} and PASSWORD_MAX_LENGTH = {int}")
    fun setBounds(min: Int, max: Int) {
        minLength = min
        maxLength = max
    }

    @And("PASSWORD_MIN_LENGTH = {int}")
    fun setMin(min: Int) {
        minLength = min
    }

    @And("PASSWORD_MAX_LENGTH = {int}")
    fun setMax(max: Int) {
        maxLength = max
    }

    @Given("""environment property "{word}" is not "{word}"""")
    fun checkEnvironment(prop: String, forbidden: String) {
        val value = System.getProperty(prop)
        assumeTrue(value != forbidden)
    }

    @When(
        "I validate credentials with email {string} and password {string} and name {string}"
    )
    fun validateFull(email: String, password: String, name: String) = runTest {
        result = useCase.execute(
            ValidateUserCredentialsUseCase.Params(email, password, name)
        )
    }

    @When("I validate credentials with email {string} and password {string}")
    fun validateEmailPassword(email: String, password: String) = runTest {
        result = useCase.execute(
            ValidateUserCredentialsUseCase.Params(email, password)
        )
    }

    @When("I validate credentials with password of length PASSWORD_MIN_LENGTH - 1")
    fun validateShortPassword() = runTest {
        val short = "p".repeat(minLength - 1)
        result = useCase.execute(
            ValidateUserCredentialsUseCase.Params("test@test.ru", short)
        )
    }

    @When("I validate credentials with password of length PASSWORD_MIN_LENGTH")
    fun validatePasswordMin() = runTest {
        val password = "p".repeat(PASSWORD_MIN_LENGTH)
        result = useCase.execute(
            ValidateUserCredentialsUseCase.Params("test@test.ru", password)
        )
    }

    @When("I validate credentials with password of length PASSWORD_MAX_LENGTH")
    fun validatePasswordMax() = runTest {
        val password = "p".repeat(PASSWORD_MAX_LENGTH)
        result = useCase.execute(
            ValidateUserCredentialsUseCase.Params("test@test.ru", password)
        )
        Log.i("Cucumber", "The result is: ${result?.let { result!!::class.simpleName }}\nThe password length is: ${password.length}")
    }

    @When("I validate credentials with password of length PASSWORD_MAX_LENGTH + 1")
    fun validatePasswordTooLong() = runTest {
        val password = "p".repeat(maxLength + 1)
        result = useCase.execute(
            ValidateUserCredentialsUseCase.Params("test@test.ru", password)
        )
    }

    @Then("the result should be Success")
    fun assertSuccess() {
        assertTrue(result is ValidateResult.Success, "The result is: ${result?.let { result!!::class.simpleName }}")
    }

    @Then("the result should be Error {string}")
    fun assertErrorMessage(msg: String) {
        assertTrue(result is ValidateResult.Error)
        assertEquals(msg, (result as ValidateResult.Error).message)
    }

    @Then("the result should be Error")
    fun assertError() {
        assertTrue(result is ValidateResult.Error)
    }

    @Then("the result should not be Error")
    fun assertNotError() {
        assertFalse(result is ValidateResult.Error)
    }
}
