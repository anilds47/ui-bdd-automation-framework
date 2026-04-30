Feature: API Functionality Tests
  As a tester, I want to verify API endpoints for data retrieval and creation.

  Scenario: Retrieve a post from JSONPlaceholder
    Given the API base URI is "https://jsonplaceholder.typicode.com"
    When I send a GET request to "/posts/1" with retry filter
    Then the response should be received

  Scenario: Create a user via POST to ReqRes
    Given the API base URI is "https://reqres.in"
    When I send a POST request to "/api/users" with payload and retry filter
    Then the response status should be 201
