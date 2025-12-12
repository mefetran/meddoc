package mefetran.dgusev.meddocs.data.test

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["features"],
    glue = ["mefetran.dgusev.meddocs.data.test.steps"],
)
class DataCucumberTests