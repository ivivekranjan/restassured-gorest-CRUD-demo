import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;
import io.restassured.http.ContentType;

public class UserAPITests {

    static RequestSpecification reqSpec;
    static String token = "Bearer 621d9f7081f784fc2aafbeef5527b14555e162b4edeb60d29133a0799164b70a"; // Replace with your actual GoREST token

    public static void main(String[] args) {

        // Setup requestSpecification
        reqSpec = new RequestSpecBuilder()
                .setBaseUri("https://gorest.co.in/public/v2")
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", token)
                .build();

        // 1️ POST - Create User
        String payload = "{ \"name\": \"Vivek\", \"gender\": \"male\", \"email\": \"vivek" + System.currentTimeMillis() + "@test.com\", \"status\": \"active\" }";

        Response postResponse = given()
                .spec(reqSpec)
                .body(payload)
        .when()
                .post("/users")
        .then()
                .statusCode(201)
                .body("name", equalTo("Vivek"))
                .extract().response();

        String userId = postResponse.jsonPath().getString("id");
        System.out.println("Created User ID: " + userId);

        // 2️ PUT - Update user
        String updatePayload = "{ \"name\": \"Vivek Ranjan\", \"gender\": \"male\", \"email\": \"vivek" + System.currentTimeMillis() + "@test.com\", \"status\": \"inactive\" }";

        given()
                .spec(reqSpec)
                .body(updatePayload)
        .when()
                .put("/users/" + userId)
        .then()
                .statusCode(200)
                .body("status", equalTo("inactive"));

        // 3️ GET - Fetch updated user & extract status
        Response getResponse = given()
                .spec(reqSpec)
        .when()
                .get("/users/" + userId)
        .then()
                .statusCode(200)
                .extract().response();

        String userStatus = getResponse.jsonPath().getString("status");
        System.out.println("User Status after update: " + userStatus);

        // 4️ DELETE - Remove user
        given()
                .spec(reqSpec)
        .when()
                .delete("/users/" + userId)
        .then()
                .statusCode(204); // No Content
    }
}

