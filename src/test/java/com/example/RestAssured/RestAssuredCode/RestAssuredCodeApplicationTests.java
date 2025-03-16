package com.example.RestAssured.RestAssuredCode;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("API Testing")  // 🔹 High-level Epic
@Feature("Laptop Management API")  // 🔹 Specific API feature
public class RestAssuredCodeApplicationTests {

	private static int laptopId = 57;  // 🔹 Store dynamically created ID

	@BeforeAll
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8080";
	}

	// ✅ Test 1: Get All Laptops
	@Test
	@Order(1)
	@DisplayName("Test GET /laptops - Fetch all laptops")
	@Severity(SeverityLevel.NORMAL)
	@Story("Fetch all available laptops")
	@Description("This test fetches all laptops and verifies response structure.")
	@Step("Execute GET request to fetch all laptops")
	public void testGetAllLaptops() {
		given()
				.when()
				.get("/laptops")
				.then()
				.statusCode(200)
				.contentType(ContentType.JSON);
	}

	// ✅ Test 2: Add New Laptop (Store ID for Later Tests)
	@Test
	@Order(2)
	@DisplayName("Test POST /laptops - Insert new laptop")
	@Severity(SeverityLevel.CRITICAL)
	@Story("Create a new laptop entry")
	@Description("This test adds a new laptop to the database and verifies the response.")
	@Step("Execute POST request to create a new laptop")
	public void testAddLaptop() {
		String requestBody = """
            {
                "laptopBrand": "Mac Book Pro",
                "modelName": "M2",
                "laptopTag": "Apple"
            }
        """;

		Response response = given()
				.contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post("/laptops")
				.then()
				.log().all()
				.assertThat()
				.statusCode(201)
				.extract()
				.response();

		// ✅ Extract ID for future tests
		laptopId = response.path("id");
		Assertions.assertNotNull(laptopId, "Laptop ID should not be null!");

		System.out.println("Created Laptop ID: " + laptopId);
	}

	// ✅ Test 3: Update Laptop (Using Stored ID)
	@Test
	@Order(3)
	@DisplayName("Test PUT /laptops/{id} - Update existing laptop")
	@Severity(SeverityLevel.NORMAL)
	@Story("Update an existing laptop entry")
	@Description("This test updates an existing laptop's details and verifies the response.")
	@Step("Execute PUT request to update a laptop")
	public void testUpdateLaptop() {
		String updatedRequestBody = """
            {
                "laptopBrand": "Mac Book Pro Version Update",
                "modelName": "M3 New Model Update",
                "laptopTag": "Apple"
            }
        """;

		given()
				.contentType(ContentType.JSON)
				.body(updatedRequestBody)
				.when()
				.put("/laptops/" + laptopId)
				.then()
				.log().all()
				.assertThat()
				.statusCode(200)
				.body("modelName", equalTo("M3 New Model Update"));  // ✅ Verify Update
	}

	// ✅ Test 4: Delete Laptop (Verify Deletion)
	@Test
	@Order(4)
	@DisplayName("Test DELETE /laptops/{id} - Remove laptop")
	@Severity(SeverityLevel.CRITICAL)
	@Story("Delete a laptop entry")
	@Description("This test deletes a laptop by ID and verifies deletion by checking GET response.")
	@Step("Execute DELETE request to remove a laptop")
	public void testDeleteLaptop() {
		given()
				.when()
				.delete("/laptops/" + laptopId)
				.then()
				.log().all()
				.assertThat()
				.statusCode(200);  // ✅ Expect No Content instead of 200

		// ✅ Verify Deletion (GET should return 404)
		given()
				.when()
				.get("/laptops/" + laptopId)
				.then()
				.statusCode(404);
	}
}
