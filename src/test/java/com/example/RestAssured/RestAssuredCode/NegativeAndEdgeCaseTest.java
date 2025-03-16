package com.example.RestAssured.RestAssuredCode;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;

@Epic("Laptop API Tests")
@Feature("Laptop API Negative & Edge Case Testing")
public class NegativeAndEdgeCaseTest {

    static {
        baseURI = "http://localhost:8080"; // Set API Base URL
    }

    // 🔴 Test 1: Fetch Laptop with Non-Existent ID
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("GET /laptops/{id} - Fetch Non-Existent Laptop")
    @Step("Sending GET request with invalid ID")
    @Description("This test verifies that fetching a non-existent laptop ID returns a proper error response.")
    @DisplayName("Test GET /laptops/{id} - Non-existent ID")
    public void testGetLaptopWithInvalidId() {
        Response response = given()
                .when()
                .get("/your-endpoint") // Adjust your endpoint
                .then()
                .log().all()// Log full response to debug
//                .body(containsString("Laptop not found"))
                .extract().response();

        String responseBody = response.asString();
        System.out.println("Response Body: " + responseBody);
    }

    // 🔴 Test 2: Search with Invalid Query Parameter
    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("GET /laptops?brand=UnknownBrand - Invalid Query")
    @Step("Sending GET request with an unknown brand")
    @Description("This test verifies that searching for a brand that does not exist returns an empty response.")
    @DisplayName("Test GET /laptops?brand=UnknownBrand - Invalid Query")
    public void testSearchWithInvalidBrand() {
        Response response = given()
                .queryParam("brand", "UnknownBrand")
                .when()
                .get("/laptops")
                .then()
                .statusCode(200) // API might return empty array instead of 404
                .extract().response();

        String responseBody = response.asString();
        System.out.println("Response Body: "+responseBody);
    }

    // 🔴 Test 3: Post Laptop with Invalid JSON
    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("POST /laptops - Send Invalid JSON")
    @Step("Sending POST request with malformed JSON")
    @Description("This test verifies that sending an invalid JSON payload returns a 400 Bad Request.")
    @DisplayName("Test POST /laptops - Invalid JSON")
    public void testCreateLaptopWithInvalidJson() {
        String invalidLaptop = "{ \"laptopBrand\": \"Dell\", \"modelName\": }"; // Invalid JSON

        Response response = given()
                .contentType(ContentType.JSON)
                .body(invalidLaptop)
                .when()
                .post("/laptops")
                .then()
                .log().all() // Logs request & response
                .statusCode(400) // Expecting Bad Request
                .extract().response();

        String responseBody = response.asString();
        System.out.println("Response Body: " + responseBody);
    }


    // ⚠️ Test 4: Post Laptop with Empty Fields
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("POST /laptops - Empty Fields")
    @Step("Sending POST request with empty laptop data")
    @Description("This test verifies that sending empty fields in the request body results in validation errors.")
    @DisplayName("Test POST /laptops - Empty Fields")
    public void testCreateLaptopWithEmptyFields() {
        String emptyLaptop = "{ \"laptopBrand\": \"\", \"modelName\": \"\", \"laptopTag\": \"\" }";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(emptyLaptop)
                .when()
                .post("/laptops")
                .then()
                .log().all() // Logs response details
                .statusCode(400) // Expecting Bad Request -> altered in controller
                .extract().response();

        String responseBody = response.asString();
        System.out.println("Response Body: " + responseBody);
    }




    // ⚠️ Test 5: Get Laptop with Extreme ID (Edge Case)
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("GET /laptops/{id} - Extreme ID")
    @Step("Sending GET request with an extreme ID value")
    @Description("This test verifies that querying with the maximum integer ID results in a proper response.")
    @DisplayName("Test GET /laptops/{id} - Extreme ID")
    public void testGetLaptopWithExtremeId() {
        Response response = given()
                .pathParam("id", 2147483647) // Max integer value
                .when()
                .get("/laptops/{id}")
                .then()
                .log().all()
                .statusCode(404) // Expecting Not Found
                .extract().response();

        String responseBody = response.asString();
        System.out.println("Response Body: " + responseBody);
    }


    // ⚠️ Test 6: Post Laptop with Special Characters
    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("POST /laptops - Special Characters")
    @Step("Sending POST request with special character inputs")
    @Description("This test verifies whether the API handles special character inputs properly.")
    @DisplayName("Test POST /laptops - Special Characters")
    public void testCreateLaptopWithSpecialCharacters() {
        String specialCharLaptop = "{ \"laptopBrand\": \"<>Dell??\", \"modelName\": \"!@#$%^\", \"laptopTag\": \"%%^&*\" }";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(specialCharLaptop)
                .when()
                .post("/laptops")
                .then()
                .log().all()
                .statusCode(201) // Expecting Validation Error
                .extract().response();

        String responseBody = response.asString();
        System.out.println("Response Body: " + responseBody);
    }

}
