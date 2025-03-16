package com.example.RestAssured.RestAssuredCode;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class BasicScenarioTest {

    static {
        baseURI = "http://localhost:8080"; // Set Base URL
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Retrieve all available laptops")
    @Step("Send GET request to fetch all laptops")
    @Description("Verify that GET /laptops returns a list of laptops with valid details")
    @DisplayName("Test GET /laptops - Fetch all laptops")
    public void testGetAllLaptops() {
        given()
                .when()
                .get("/laptops")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0)) // Ensure at least one laptop exists
                .body("[0].id", notNullValue())
                .body("[0].laptopBrand", notNullValue())
                .body("[0].modelName", notNullValue())
                .body("[0].laptopTag", notNullValue());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Retrieve a laptop by its ID")
    @Step("Send GET request to fetch a laptop by ID")
    @Description("Verify that GET /laptops/{id} returns the correct laptop details")
    @DisplayName("Test GET /laptops/{id} - Fetch a specific laptop")
    public void testGetLaptopById() {
        int laptopId = 5; // Update this to an existing ID

        given()
                .pathParam("id", laptopId)
                .when()
                .get("/laptops/{id}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(laptopId))
                .body("laptopBrand", notNullValue())
                .body("modelName", notNullValue())
                .body("laptopTag", notNullValue());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Filter laptops by brand")
    @Step("Send GET request to filter laptops by brand")
    @Description("Verify that GET /laptops?brand=Lenovo returns only Lenovo laptops")
    @DisplayName("Test GET /laptops?brand=Lenovo - Filter by brand")
    public void testSearchLaptopsByBrand() {
        given()
                .queryParam("brand", "Lenovo")
                .when()
                .get("/laptops")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0)) // Ensure at least one laptop is returned
                .body("findAll { it.laptopBrand == 'Lenovo' }.size()", greaterThan(0)); // Ensure at least one match
    }


}

