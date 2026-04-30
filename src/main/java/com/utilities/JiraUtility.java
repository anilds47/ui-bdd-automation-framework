package com.utilities;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class JiraUtility {

    private static final String BASE_URL =ConfigReader.getValue("Base_Url");
    private static final String AUTH_TOKEN =ConfigReader.getValue("AuthKey");
    private static final String SCRUM_KEY=ConfigReader.getValue("ProjectKey");

    private static final String ISSUE_ENDPOINT = ConfigReader.getValue("Issue_Endpoint");


    private static Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", AUTH_TOKEN);
         // Required for file uploads
        return headers;
    }

    public static String createJiraIssue(String summary, String issueType) {
          String requestBody = "{ \"fields\": { \"project\": { \"key\": \""+SCRUM_KEY+"\" }, " +
                "\"summary\": \"" + summary + "\", " +
                "\"issuetype\": { \"name\": \"" + issueType + "\" } } }";

        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Content-Type","application/json")
                .headers(getHeaders())  // Use the headers HashMap
                .body(requestBody)
                .when()
                .post(ISSUE_ENDPOINT)
                .then()
                .assertThat().statusCode(201)
                .extract().response();

        String issueId = response.jsonPath().getString("id");
        System.out.println("✅ Jira Issue Created: " + issueId);
        return issueId;
    }

    // ✅ Attach Screenshot to Jira Issue
    public static void attachFileToJiraIssue(String issueId, String filePath) {
        RestAssured.given()
                .baseUri(BASE_URL)
                .headers(getHeaders())
                . header("X-Atlassian-Token", "no-check") // Use the headers HashMap
                .pathParam("key", issueId)
                .multiPart("file", new File(filePath))
                .when()
                .post(ISSUE_ENDPOINT+"/{key}/attachments")
                .then()
                .assertThat().statusCode(200);

        System.out.println("📎 Screenshot attached to Jira Issue: " + issueId);
    }
}