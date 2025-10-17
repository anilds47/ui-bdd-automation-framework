package com.testcases;

import com.apiPages.Payload;
import com.utilities.ApiRetryAndLogFilter;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class ApiTestcases {

    @Test
    public void test1(){
        Response response = RestAssured
                .given()
                .filter(new ApiRetryAndLogFilter(3))  // Attach Listener
                .baseUri("https://jsonplaceholder.typicode.com")
                .basePath("/posts/1")
                .get();
    }
    @Test
    public void test2(){
        Response response = RestAssured.given()
                .filter(new ApiRetryAndLogFilter(3))
                .baseUri("https://reqres.in")
                .basePath("/api/users")
                .header("Content-Type", "application/json")
                .body(Payload.request())
                .post();

        // You can add assertions here
        response.then().statusCode(201);
    }
}
