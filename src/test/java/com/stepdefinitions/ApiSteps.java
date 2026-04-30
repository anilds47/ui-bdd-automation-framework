package com.stepdefinitions;

import com.apiPages.Payload;
import com.utilities.ApiRetryAndLogFilter;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiSteps {
    private Response response;

    @Given("the API base URI is {string}")
    public void setBaseUri(String uri) {
        RestAssured.baseURI = uri;
    }

    @When("I send a GET request to {string} with retry filter")
    public void sendGetRequest(String path) {
        response = RestAssured.given()
                .filter(new ApiRetryAndLogFilter(3))
                .basePath(path)
                .get();
    }

    @When("I send a POST request to {string} with payload and retry filter")
    public void sendPostRequest(String path) {
        response = RestAssured.given()
                .filter(new ApiRetryAndLogFilter(3))
                .header("Content-Type", "application/json")
                .body(Payload.request())
                .post(path);
    }

    @Then("the response should be received")
    public void verifyResponseReceived() {
        // Add any basic checks if needed, e.g., response != null
    }

    @Then("the response status should be {int}")
    public void verifyStatusCode(int status) {
        response.then().statusCode(status);
    }
}
