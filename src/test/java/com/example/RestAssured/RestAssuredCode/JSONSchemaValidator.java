package com.example.RestAssured.RestAssuredCode;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class JSONSchemaValidator {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Fetch all laptops from the system")
    @Step("Send GET request to /laptops and validate response schema")
    @Description("This test retrieves all laptops and validates the response structure using JSON schema validation.")
    @DisplayName("Test GET /laptops - Fetch all laptops")
    public void testGetAllLaptops() {
        baseURI = "http://localhost:8080";
        given().get("/laptops")
                .then()
                .statusCode(200)
                .assertThat().body(matchesJsonSchemaInClasspath("schemaValidator.json"));

    }
}
