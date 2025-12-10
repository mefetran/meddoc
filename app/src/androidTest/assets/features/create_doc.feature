Feature: Create document

  Scenario: Successful login
    Given I open the login screen
    When I type username "test@test.ru"
    And I type password "hrenaga1"
    And I tap the login button
    Then I should see Home screen

  Scenario: Creating of document
    Given I see document home screen
    When I tap on create document icon
    Then I should see create document screen
    When I type name "Test document" of document
    And I type description "Test description"
    And I type in add info key "test key"
    And I type in add info value "test value"
    And I tap on Add button
    When I tap create document button
    Then I should see Home screen

  Scenario: Successful logout
    Given I see settings button
    When I tap on it
    Then I should see Settings screen
    When I tap on logout button
    Then I should see Login screen
