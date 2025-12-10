Feature: Login screen

  Scenario: Successful login
    Given I open the login screen
    When I type username "test@test.ru"
    And I type password "hrenaga1"
    And I tap the login button
    Then I should see Home screen