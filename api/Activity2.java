package activities;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class Activity2 {
    String baseURI = "https://petstore.swagger.io/v2/user";

    @Test(priority = 1)
    public void postRequestTest() throws IOException {

        // Import JSON file
        FileInputStream inputJSON = new FileInputStream("src/test/resources/data.json");
        // Read JSON file as String
        String reqBody = new String(inputJSON.readAllBytes());

        Response response =
                given().contentType(ContentType.JSON) // Set headers
                        .body(reqBody) // Pass request body from file
                        .when().post(baseURI); // Send POST request

        inputJSON.close();

        // Assertion
        response.then().body("code", equalTo(200));
        response.then().body("message", equalTo("890"));

    }

    @Test(priority = 2)
    public void getRequestTest(){
        // Import JSON file to write to
        File outputJSON = new File("src/test/resources/userGETResponse.json");

        Response response =
                given().contentType(ContentType.JSON) // Set headers
                        .pathParam("username", "jagruti_badhan") // Pass request body from file
                        .when().get(baseURI + "/{username}"); // Send GET request

        // Get response body
        String resBody = response.getBody().asPrettyString();

        try {
            // Create JSON file
            outputJSON.createNewFile();
            // Write response body to external file
            FileWriter writer = new FileWriter(outputJSON.getPath());
            writer.write(resBody);
            writer.close();
        } catch (IOException excp) {
            excp.printStackTrace();
        }

        // Assertion
        response.then().body("id", equalTo(890));
        response.then().body("username", equalTo("jagruti_badhan"));
        response.then().body("firstName", equalTo("jagruti"));
        response.then().body("lastName", equalTo("badhan"));
        response.then().body("email", equalTo("justincase@mail.com"));
        response.then().body("password", equalTo("password123"));
        response.then().body("phone", equalTo("9812763450"));
    }

    @Test(priority = 3)
    public void deleteRequestTest(){
        Response response =
                given().contentType(ContentType.JSON) // Set headers
                        .pathParam("username", "jagruti_badhan") // Add path parameter
                        .when().delete(baseURI + "/{username}"); // Send DELETE request

        // Assertion
        response.then().body("code", equalTo(200));
        response.then().body("message", equalTo("jagruti_badhan"));

    }

}
