Feature: Login screen

  Scenario: Successful login
    Given I open the login screen
    When I type username "test@test.ru"
    And I type password "hrenaga1"
    And I tap the login button
    Then I should see Home screen

  Scenario: Successful logout
    Given I see settings button
    When I tap on it
    Then I should see Settings screen
    When I tap on logout button
    Then I should see Login screen
