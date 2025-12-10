package mefetran.dgusev.meddocs.test

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["features"],
    glue = ["mefetran.dgusev.meddocs.test.steps", "mefetran.dgusev.meddocs.test.holder"],
)
class CucumberTests
